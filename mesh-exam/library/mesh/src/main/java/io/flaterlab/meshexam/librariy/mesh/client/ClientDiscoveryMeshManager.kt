package io.flaterlab.meshexam.librariy.mesh.client

import android.content.Context
import com.google.android.gms.nearby.Nearby
import com.google.android.gms.nearby.connection.ConnectionsClient
import com.google.android.gms.nearby.connection.DiscoveryOptions
import com.google.android.gms.nearby.connection.Payload
import com.google.android.gms.nearby.connection.Strategy
import com.google.gson.GsonBuilder
import io.flaterlab.meshexam.librariy.mesh.client.exception.MeshConnectionException
import io.flaterlab.meshexam.librariy.mesh.common.ConnectionsLifecycleAdapterCallback
import io.flaterlab.meshexam.librariy.mesh.common.EndpointDiscoveryAdapterCallback
import io.flaterlab.meshexam.librariy.mesh.common.dto.*
import io.flaterlab.meshexam.librariy.mesh.common.parser.AdvertiserInfoJsonParser
import io.flaterlab.meshexam.librariy.mesh.common.parser.JsonParserHelper
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import timber.log.Timber

internal class ClientDiscoveryMeshManager(
    private val serviceId: String,
    private val nearby: ConnectionsClient,
    private val discoveryCallback: EndpointDiscoveryAdapterCallback,
    private val connectionsCallback: ConnectionsLifecycleAdapterCallback<AdvertiserInfo>,
    private val payloadCallback: ClientPayloadAdapterCallback,
    private val jsonParserHelper: JsonParserHelper,
) : EndpointDiscoveryAdapterCallback.AdapterCallback,
    ConnectionsLifecycleAdapterCallback.AdapterCallback<AdvertiserInfo> {

    var onDisconnectedListener: (AdvertiserInfo, ClientInfo) -> Unit = { _, _ -> }

    private var selfInfo: ClientInfo? = null
    private var joinResult: CompletableDeferred<ParentInfo>? = null
    private var parentInfo: ParentInfo? = null
    private var onAdvertiserSetChangeListener: ((MeshResult<ClientMesh>) -> Unit)? = null

    private val advertiserInfoCache = AdvertiserInfoCache()

    init {
        discoveryCallback.adapterCallback = this
        connectionsCallback.adapterCallback = this
    }

    fun discoverExams(): Flow<ClientMesh> = callbackFlow {
        onAdvertiserSetChangeListener = { result ->
            when (result) {
                is MeshResult.Success -> trySend(result.data)
                is MeshResult.Error -> throw result.cause
            }
        }
        discover()
        awaitClose { stopDiscovery() }
    }

    private fun discover() {
        val options = DiscoveryOptions.Builder()
            .setStrategy(Strategy.P2P_CLUSTER)
            .build()
        nearby.startDiscovery(serviceId, discoveryCallback, options)
            .addOnSuccessListener {
                Timber.d("Discovery started successfully...")
            }
            .addOnFailureListener { e ->
                Timber.d("Discovery failed: $e")
                onAdvertiserSetChangeListener!!(MeshResult.Error(e))
            }
    }

    override fun onAdvertiserFound(endpointId: String, advertiserInfo: AdvertiserInfo) {
        advertiserInfoCache[endpointId] = advertiserInfo
        emmitAdvertisers()
    }

    override fun onAdvertiserLost(endpointId: String) {
        advertiserInfoCache.remove(endpointId)?.also { emmitAdvertisers() }
    }

    private fun emmitAdvertisers() {
        Timber.d("Emitting advertisers: $advertiserInfoCache")
        onAdvertiserSetChangeListener!!(
            MeshResult.Success(
                ClientMesh(advertiserInfoCache.getUniqueListByExamId())
            )
        )
    }

    private fun stopDiscovery() {
        nearby.stopDiscovery()
        advertiserInfoCache.clear()
        onAdvertiserSetChangeListener = null
    }

    suspend fun joinExam(examId: String, clientInfo: ClientInfo): AdvertiserInfo =
        coroutineScope {
            selfInfo = clientInfo
            val endpointId = advertiserInfoCache.getEndpointByExamId(examId)
                ?: throw IllegalArgumentException("Exam with id $examId does not exist")
            join(endpointId)
            try {
                CompletableDeferred<ParentInfo>()
                    .also { joinResult = it }
                    .await()
                    .also { parentInfo = it }
                    .second
            } finally {
                joinResult = null
                stopDiscovery()
            }
        }

    private fun join(endpointId: String) {
        val info = jsonParserHelper
            .clientInfoJsonParser
            .toJson(selfInfo!!)
            .toByteArray()
        nearby.requestConnection(info, endpointId, connectionsCallback)
            .addOnFailureListener(::throwConnectionException)
    }

    override fun onRequested(endpointId: String, info: AdvertiserInfo) {
        nearby.acceptConnection(endpointId, payloadCallback)
            .addOnFailureListener(::throwConnectionException)
    }

    override fun onConnected(endpointId: String) {
        val advertiserInfo = advertiserInfoCache[endpointId]
            ?: throw IllegalStateException(
                "Advertiser mustn't be null when connected. Endpoint: $endpointId," +
                        " Cache: $advertiserInfoCache"
            )
        joinResult?.complete(endpointId to advertiserInfo)
    }

    override fun onRejected(endpointId: String) = join(endpointId)

    override fun onError(endpointId: String) = throwConnectionException()

    override fun onDisconnected(endpointId: String) {
        onDisconnectedListener(
            parentInfo?.second
                ?: throw IllegalStateException(
                    "Parent info mustn't be null. Cache $advertiserInfoCache"
                ),
            selfInfo
                ?: throw IllegalStateException(
                    "Self info mustn't be null. Cache $advertiserInfoCache"
                )
        )
    }

    override fun rejectConnection(endpointId: String) {
        nearby.rejectConnection(endpointId)
    }

    private fun throwConnectionException(cause: Exception? = null) {
        joinResult?.completeExceptionally(provideConnectionException(cause))
    }

    private fun provideConnectionException(cause: Exception? = null) =
        MeshConnectionException(cause)

    suspend fun notifyClientConnected(info: ClientInfo) {
        Timber.d("Notifying client connected: $info")
        val dataJson = jsonParserHelper
            .clientConnectedJsonParser
            .toJson(MeshData.ClientConnected(info, selfInfo!!.id))
        val meshPayload = MeshPayload(
            type = MeshPayload.ContentType.CLIENT_CONNECTED,
            data = dataJson,
        )
        val bytes = jsonParserHelper
            .meshPayloadJsonParser
            .toJson(meshPayload)
            .toByteArray()
        nearby.sendPayload(parentInfo!!.first, Payload.fromBytes(bytes)).await()
    }

    suspend fun notifyClientDisconnected(info: ClientInfo) {
        Timber.d("Notifying client disconnected: $info")
        val dataJson = jsonParserHelper
            .clientDisconnectedJsonParser
            .toJson(MeshData.ClientDisconnected(info))
        val meshPayload = MeshPayload(
            type = MeshPayload.ContentType.CLIENT_DISCONNECTED,
            data = dataJson,
        )
        val bytes = jsonParserHelper
            .meshPayloadJsonParser
            .toJson(meshPayload)
            .toByteArray()
        nearby.sendPayload(parentInfo!!.first, Payload.fromBytes(bytes)).await()
    }

    suspend fun forwardBytes(bytes: ByteArray) {
        nearby.sendPayload(parentInfo!!.first, Payload.fromBytes(bytes)).await()
    }

    companion object {
        fun getInstance(context: Context): ClientDiscoveryMeshManager =
            GsonBuilder()
                .excludeFieldsWithoutExposeAnnotation()
                .create()
                .let { gson ->
                    ClientDiscoveryMeshManager(
                        serviceId = context.packageName,
                        nearby = Nearby.getConnectionsClient(context),
                        discoveryCallback = EndpointDiscoveryAdapterCallback(gson),
                        connectionsCallback = ConnectionsLifecycleAdapterCallback(
                            AdvertiserInfoJsonParser(gson)
                        ),
                        payloadCallback = ClientPayloadAdapterCallback(gson),
                        jsonParserHelper = JsonParserHelper.getInstance(gson)
                    )
                }
    }
}