package io.flaterlab.meshexam.librariy.mesh.host

import android.content.Context
import com.google.android.gms.nearby.Nearby
import com.google.android.gms.nearby.connection.AdvertisingOptions
import com.google.android.gms.nearby.connection.ConnectionsClient
import com.google.android.gms.nearby.connection.Payload
import com.google.android.gms.nearby.connection.Strategy
import com.google.gson.GsonBuilder
import io.flaterlab.meshexam.librariy.mesh.common.ConnectionsLifecycleAdapterCallback2
import io.flaterlab.meshexam.librariy.mesh.common.LOW_ENERGY
import io.flaterlab.meshexam.librariy.mesh.common.dto.*
import io.flaterlab.meshexam.librariy.mesh.common.parser.AdvertiserInfoJsonParser
import io.flaterlab.meshexam.librariy.mesh.common.parser.ClientInfoJsonParser
import io.flaterlab.meshexam.librariy.mesh.common.parser.FromHostPayloadParser
import io.flaterlab.meshexam.librariy.mesh.common.parser.JsonParser
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.tasks.await
import timber.log.Timber

class HostMeshManager internal constructor(
    private val serviceId: String,
    private val nearby: ConnectionsClient,
    private val connectionsCallback: ConnectionsLifecycleAdapterCallback2<ClientInfo>,
    private val payloadCallback: HostPayloadAdapterCallback,
    private val advertiserJsonParser: JsonParser<AdvertiserInfo>,
    private val fromHostPayloadParser: JsonParser<FromHostPayload>,
) : ConnectionsLifecycleAdapterCallback2.AdapterCallback<ClientInfo>,
    HostPayloadAdapterCallback.AdapterCallback {

    private var advertiserInfo: AdvertiserInfo? = null
    private var onClientSetChangeListener: ((MeshResult<HostMesh>) -> Unit)? = null
    private val left = MeshList(isPositiveDirection = false)
    private val right = MeshList(isPositiveDirection = true)

    private val children = MeshHand()

    private val _connectedClientsFlow = MutableStateFlow<List<ClientInfo>>(emptyList())
    val connectedClientsFlow: StateFlow<List<ClientInfo>> = _connectedClientsFlow

    var onPayloadFromClientListener: ((FromClientPayload) -> Unit)? = null

    init {
        connectionsCallback.adapterCallback = this
        payloadCallback.adapterCallback = this
    }

    fun create(info: AdvertiserInfo): Flow<HostMesh> = callbackFlow {
        Timber.d("Creating a mesh: advertiser = $info")
        advertiserInfo = info
        onClientSetChangeListener = { result ->
            if (isActive) {
                when (result) {
                    is MeshResult.Success -> trySend(result.data)
                    is MeshResult.Error -> close(result.cause.also(Timber::e))
                }
            }
        }
        advertise()
        awaitClose {
            onClientSetChangeListener = null
            stopAdvertising()
        }
    }

    fun stop() {
        Timber.d("Stopping advertising...")
        stopAdvertising()
        nearby.stopAllEndpoints()
        advertiserInfo = null
        left.clear()
        right.clear()
    }

    private fun advertise() {
        val info = advertiserInfo!!
        val infoBytes = advertiserJsonParser.toJson(info)
            .also { Timber.d("Name json: $it") }
            .toByteArray()
        val options = AdvertisingOptions.Builder()
            .setStrategy(Strategy.P2P_CLUSTER)
            .setLowPower(LOW_ENERGY)
            .setDisruptiveUpgrade(false)
            .build()
        nearby.startAdvertising(infoBytes, serviceId, connectionsCallback, options)
            .addOnSuccessListener {
                Timber.d("Advertising has stared successfully...")
            }
            .addOnFailureListener { e ->
                onClientSetChangeListener?.invoke(MeshResult.Error(e))
            }
    }

    private fun stopAdvertising() {
        nearby.stopAdvertising()
    }

    override fun onRequested(endpointId: String, info: ClientInfo) {
        if (left.isEmpty() || right.isEmpty()) {
            nearby.acceptConnection(endpointId, payloadCallback)
                .addOnFailureListener { e ->
                    onClientSetChangeListener?.invoke(MeshResult.Error(e))
                }
        } else {
            nearby.rejectConnection(endpointId)
            stopAdvertising()
        }
    }

    override fun onConnected(endpointId: String, info: ClientInfo) {
        Timber.d("Connected: $endpointId")
        clientConnected(info)
        children.addEndpointId(endpointId)
    }

    override fun onRejected(endpointId: String, info: ClientInfo) {
        Timber.d("Rejected: $endpointId -> $info")
    }

    override fun onError(endpointId: String, info: ClientInfo) {
        Timber.d("Error: $endpointId -> $info")
    }

    override fun onDisconnected(endpointId: String, info: ClientInfo) {
        Timber.d("Disconnected: $endpointId -> $info")
        clientDisconnected(info)
        children.removeEndpointId(endpointId)
        if (left.isEmpty() xor right.isEmpty()) advertise()
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
        emmitClients()
    }

    private fun clientDisconnected(clientInfo: ClientInfo) {
        left.remove(clientInfo)
        right.remove(clientInfo)
        emmitClients()
    }

    private fun emmitClients() {
        onClientSetChangeListener?.invoke(
            MeshResult.Success(
                HostMesh(getConnectedClients())
            )
        )
        _connectedClientsFlow.value = getConnectedClients()
    }

    private fun getConnectedClients(): List<ClientInfo> {
        return left.mergeByClosest(right).also { Timber.d("Emitting: $it") }
    }

    override fun onClientConnected(data: MeshData.ClientConnected) {
        Timber.d("Client connected: $data")
        clientConnected(data.clientInfo, data.parentId)
    }

    override fun onClientDisconnected(data: MeshData.ClientDisconnected) {
        Timber.d("Client disconnected: $data")
        clientDisconnected(data.clientInfo)
    }

    suspend fun sendPayload(payload: FromHostPayload) {
        coroutineScope {
            children.asList().map { endpointId ->
                val bytes = fromHostPayloadParser.toJson(payload).toByteArray()
                async { nearby.sendPayload(endpointId, Payload.fromBytes(bytes)).await() }
            }.awaitAll()
        }
    }

    override fun onPayloadReceived(payload: FromClientPayload) {
        onPayloadFromClientListener?.invoke(payload)
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
                            connectionsCallback = ConnectionsLifecycleAdapterCallback2(
                                ClientInfoJsonParser(gson)
                            ),
                            payloadCallback = HostPayloadAdapterCallback(gson),
                            advertiserJsonParser = AdvertiserInfoJsonParser(gson),
                            fromHostPayloadParser = FromHostPayloadParser(gson)
                        )
                    }
                    .also(::instance::set)
            }
    }
}