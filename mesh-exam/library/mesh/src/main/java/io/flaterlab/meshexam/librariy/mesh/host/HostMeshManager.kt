package io.flaterlab.meshexam.librariy.mesh.host

import android.content.Context
import com.google.android.gms.nearby.Nearby
import com.google.android.gms.nearby.connection.AdvertisingOptions
import com.google.android.gms.nearby.connection.ConnectionsClient
import com.google.android.gms.nearby.connection.Strategy
import com.google.gson.GsonBuilder
import io.flaterlab.meshexam.librariy.mesh.common.ConnectionsLifecycleAdapterCallback
import io.flaterlab.meshexam.librariy.mesh.common.PayloadAdapterCallback
import io.flaterlab.meshexam.librariy.mesh.common.dto.*
import io.flaterlab.meshexam.librariy.mesh.common.parser.AdvertiserInfoJsonParser
import io.flaterlab.meshexam.librariy.mesh.common.parser.ClientInfoJsonParser
import io.flaterlab.meshexam.librariy.mesh.common.parser.JsonParser
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import timber.log.Timber

class HostMeshManager internal constructor(
    private val serviceId: String,
    private val nearby: ConnectionsClient,
    private val connectionCallback: ConnectionsLifecycleAdapterCallback<ClientInfo>,
    private val payloadCallback: PayloadAdapterCallback,
    private val advertiserJsonParser: JsonParser<AdvertiserInfo>,
) : ConnectionsLifecycleAdapterCallback.AdapterCallback<ClientInfo>,
    PayloadAdapterCallback.AdapterCallback {

    private var advertiserInfo: AdvertiserInfo? = null
    private val left = MeshList()
    private val right = MeshList()
    private val clientInfoCache: MutableMap<String, ClientInfo> = HashMap()
    private val clientFlow = MutableSharedFlow<MeshResult<HostMesh>>(1)

    init {
        connectionCallback.adapterCallback = this
        payloadCallback.adapterCallback = this
    }

    fun create(info: AdvertiserInfo): Flow<MeshResult<HostMesh>> {
        advertiserInfo = info
        advertise()
        return clientFlow
    }

    fun stop() {
        stopAdvertising()
        nearby.stopAllEndpoints()
        advertiserInfo = null
        left.clear()
        right.clear()
        clientInfoCache.clear()
        emmitClients()
    }

    private fun advertise() {
        val info = advertiserInfo!!
        val infoBytes = advertiserJsonParser.toJson(info).toByteArray()
        val options = AdvertisingOptions.Builder()
            .setStrategy(Strategy.P2P_CLUSTER)
            .setDisruptiveUpgrade(false)
            .build()
        nearby.startAdvertising(infoBytes, serviceId, connectionCallback, options)
            .addOnFailureListener { e ->
                clientFlow.tryEmit(MeshResult.Error(e))
            }
    }

    private fun stopAdvertising() {
        nearby.stopAdvertising()
    }

    override fun onRequested(endpointId: String, info: ClientInfo) {
        if (left.isEmpty() || right.isEmpty()) {
            clientInfoCache[endpointId] = info
            nearby.acceptConnection(endpointId, payloadCallback)
                .addOnFailureListener { e ->
                    clientFlow.tryEmit(MeshResult.Error(e))
                }
        } else {
            nearby.rejectConnection(endpointId)
            stopAdvertising()
        }
    }

    override fun onConnected(endpointId: String) {
        Timber.d("Connected: $endpointId")
        val clientInfo = clientInfoCache[endpointId]
        if (clientInfo == null) {
            nearby.disconnectFromEndpoint(endpointId)
        } else {
            clientConnected(clientInfo)
        }
        Timber.d("Connected: $clientInfoCache")
    }

    override fun onRejected(endpointId: String) {
        Timber.d("Rejected: $endpointId")
        clientInfoCache.remove(endpointId)
    }

    override fun onError(endpointId: String) {
        Timber.d("Error: $endpointId")
        clientInfoCache.remove(endpointId)
    }

    override fun onDisconnected(endpointId: String) {
        Timber.d("Disconnected: $clientInfoCache")
        val clientInfo = clientInfoCache.remove(endpointId)
        if (clientInfo != null) {
            left.remove(clientInfo)
            right.remove(clientInfo)
        }
        emmitClients()
        if (left.isEmpty() xor right.isEmpty()) {
            advertise()
        }
    }

    override fun rejectConnection(endpointId: String) {
        nearby.rejectConnection(endpointId)
    }

    private fun clientConnected(clientInfo: ClientInfo, parentId: String? = null) {
        when {
            left.any { it.id == parentId } -> left.add(clientInfo, parentId)
            right.any { it.id == parentId } -> right.add(clientInfo, parentId)
            left.isEmpty() -> left.add(clientInfo)
            right.isEmpty() -> right.add(clientInfo)
        }
        if (left.isNotEmpty() && right.isNotEmpty()) {
            stopAdvertising()
        }
        Timber.d("Connected left: $left")
        Timber.d("Connected right: $right")
        Timber.d("Connected cache: $clientInfoCache")
        emmitClients()
    }

    private fun emmitClients() {
        clientFlow.tryEmit(
            MeshResult.Success(
                HostMesh(
                    left.mergeByClosest(right).also { Timber.d("Emitting: $it") }
                )
            )
        )
    }

    override fun onClientConnected(data: MeshData.ClientConnected) {
        Timber.d("Client connected: $data")
        clientConnected(data.clientInfo, data.parentId)
    }

    companion object {

        @Volatile
        private var instance: HostMeshManager? = null

        fun getInstance(context: Context): HostMeshManager =
            instance ?: synchronized(this) {
                instance ?: GsonBuilder()
                    .excludeFieldsWithoutExposeAnnotation()
                    .create()
                    .let { gson ->
                        HostMeshManager(
                            serviceId = context.packageName,
                            nearby = Nearby.getConnectionsClient(context),
                            connectionCallback = ConnectionsLifecycleAdapterCallback(
                                ClientInfoJsonParser(gson)
                            ),
                            payloadCallback = PayloadAdapterCallback(gson),
                            advertiserJsonParser = AdvertiserInfoJsonParser(gson),
                        )
                    }
                    .also(::instance::set)
            }
    }
}