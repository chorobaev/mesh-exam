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
import io.flaterlab.meshexam.librariy.mesh.common.PayloadAdapterCallback
import io.flaterlab.meshexam.librariy.mesh.common.dto.*
import io.flaterlab.meshexam.librariy.mesh.common.parser.AdvertiserInfoJsonParser
import io.flaterlab.meshexam.librariy.mesh.common.parser.JsonParserHelper
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.tasks.await
import timber.log.Timber

class ClientDiscoveryMeshManager internal constructor(
    private val serviceId: String,
    private val nearby: ConnectionsClient,
    private val discoveryCallback: EndpointDiscoveryAdapterCallback,
    private val connectionsCallback: ConnectionsLifecycleAdapterCallback<AdvertiserInfo>,
    private val payloadCallback: PayloadAdapterCallback,
    private val jsonParserHelper: JsonParserHelper,
) : EndpointDiscoveryAdapterCallback.AdapterCallback,
    ConnectionsLifecycleAdapterCallback.AdapterCallback<AdvertiserInfo>,
    PayloadAdapterCallback.AdapterCallback {

    private var clientInfo: ClientInfo? = null
    private var joinResult: CompletableDeferred<ParentInfo>? = null
    private var parentInfo: ParentInfo? = null

    private val advertiserInfoCache = AdvertiserInfoCache()
    private val advertisersFlow = MutableSharedFlow<MeshResult<ClientMesh>>(1)

    init {
        discoveryCallback.adapterCallback = this
        connectionsCallback.adapterCallback = this
        payloadCallback.adapterCallback = this
    }

    fun discoverExams(): Flow<MeshResult<ClientMesh>> {
        discover()
        return advertisersFlow
    }

    private fun discover() {
        val options = DiscoveryOptions.Builder()
            .setStrategy(Strategy.P2P_CLUSTER)
            .build()
        nearby.startDiscovery(serviceId, discoveryCallback, options)
            .addOnFailureListener { e ->
                Timber.d("Discovery failed: $e")
                advertisersFlow.tryEmit(MeshResult.Error(e))
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
        Timber.d("Advertisers: $advertiserInfoCache")
        advertisersFlow.tryEmit(
            MeshResult.Success(
                ClientMesh(advertiserInfoCache.getUniqueListByExamId())
            )
        )
    }

    suspend fun joinExam(examId: String, clientInfo: ClientInfo): AdvertiserInfo =
        coroutineScope {
            this@ClientDiscoveryMeshManager.clientInfo = clientInfo
            val endpointId = advertiserInfoCache.getEndpointByExamId(examId)
            if (endpointId == null) {
                throwJoiningException()
            } else {
                join(endpointId)
            }
            try {
                CompletableDeferred<ParentInfo>()
                    .also(::joinResult::set)
                    .await()
                    .also(::parentInfo::set)
                    .second
            } finally {
                joinResult = null
                stopDiscovery()
            }
        }

    private fun join(endpointId: String) {
        val info = jsonParserHelper
            .clientInfoJsonParser
            .toJson(clientInfo!!)
            .toByteArray()
        nearby.requestConnection(info, endpointId, connectionsCallback)
            .addOnFailureListener(::throwJoiningException)
    }

    private fun stopDiscovery() {
        nearby.stopDiscovery()
        advertiserInfoCache.clear()
        emmitAdvertisers()
    }

    override fun onRequested(endpointId: String, info: AdvertiserInfo) {
        nearby.acceptConnection(endpointId, payloadCallback)
            .addOnFailureListener(::throwJoiningException)
    }

    override fun onConnected(endpointId: String) {
        val advertiserInfo = advertiserInfoCache[endpointId]
        if (advertiserInfo == null) {
            throwJoiningException()
        } else {
            joinResult?.complete(endpointId to advertiserInfo)
        }
    }

    override fun onRejected(endpointId: String) = join(endpointId)

    override fun onError(endpointId: String) = throwJoiningException()

    override fun onDisconnected(endpointId: String) = join(endpointId)

    override fun rejectConnection(endpointId: String) {
        nearby.rejectConnection(endpointId)
    }

    private fun throwJoiningException(cause: Exception? = null) {
        joinResult?.completeExceptionally(MeshConnectionException(cause))
    }

    suspend fun notifyClientJoined(info: ClientInfo) {
        Timber.d("Notifying client joined: $info")
        val parent = parentInfo!!
        val dataJson = jsonParserHelper
            .clientConnectedJsonParser
            .toJson(MeshData.ClientConnected(info, clientInfo!!.id))
        val meshPayload = MeshPayload(
            type = MeshPayload.ContentType.CLIENT_CONNECTED,
            data = dataJson,
        )
        val bytes = jsonParserHelper
            .meshPayloadJsonParser
            .toJson(meshPayload)
            .toByteArray()
        nearby.sendPayload(parent.first, Payload.fromBytes(bytes)).await()
    }

    companion object {
        @Volatile
        private var instance: ClientDiscoveryMeshManager? = null

        fun getInstance(context: Context): ClientDiscoveryMeshManager =
            instance ?: synchronized(this) {
                instance ?: GsonBuilder()
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
                            payloadCallback = PayloadAdapterCallback(gson),
                            jsonParserHelper = JsonParserHelper.getInstance(gson)
                        )
                    }
                    .also(::instance::set)
            }
    }
}