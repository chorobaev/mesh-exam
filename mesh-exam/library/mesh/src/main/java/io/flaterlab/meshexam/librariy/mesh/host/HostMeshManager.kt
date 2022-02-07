package io.flaterlab.meshexam.librariy.mesh.host

import android.content.Context
import com.google.android.gms.nearby.Nearby
import com.google.android.gms.nearby.connection.AdvertisingOptions
import com.google.android.gms.nearby.connection.ConnectionsClient
import com.google.android.gms.nearby.connection.Strategy
import com.google.gson.GsonBuilder
import io.flaterlab.meshexam.librariy.mesh.common.ConnectionsLifecycleAdapterCallback2
import io.flaterlab.meshexam.librariy.mesh.common.dto.*
import io.flaterlab.meshexam.librariy.mesh.common.parser.AdvertiserInfoJsonParser
import io.flaterlab.meshexam.librariy.mesh.common.parser.ClientInfoJsonParser
import io.flaterlab.meshexam.librariy.mesh.common.parser.JsonParser
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.isActive
import timber.log.Timber

class HostMeshManager internal constructor(
    private val serviceId: String,
    private val nearby: ConnectionsClient,
    private val connectionsCallback: ConnectionsLifecycleAdapterCallback2<ClientInfo>,
    private val payloadCallback: HostPayloadAdapterCallback,
    private val advertiserJsonParser: JsonParser<AdvertiserInfo>,
) : ConnectionsLifecycleAdapterCallback2.AdapterCallback<ClientInfo>,
    HostPayloadAdapterCallback.AdapterCallback {

    private val lock = Object()

    private var advertiserInfo: AdvertiserInfo? = null
    private var onClientSetChangeListener: ((MeshResult<HostMesh>) -> Unit)? = null
    private val left = MeshList()
    private val right = MeshList()

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
                    is MeshResult.Error -> throw result.cause
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
            .setDisruptiveUpgrade(false)
            .build()
        nearby.startAdvertising(infoBytes, serviceId, connectionsCallback, options)
            .addOnSuccessListener {
                Timber.d("Advertising has stared successfully...")
            }
            .addOnFailureListener { e ->
                onClientSetChangeListener!!(MeshResult.Error(e))
            }
    }

    private fun stopAdvertising() {
        nearby.stopAdvertising()
    }

    override fun onRequested(endpointId: String, info: ClientInfo) {
        if (left.isEmpty() || right.isEmpty()) {
            nearby.acceptConnection(endpointId, payloadCallback)
                .addOnFailureListener { e ->
                    onClientSetChangeListener!!(MeshResult.Error(e))
                }
        } else {
            nearby.rejectConnection(endpointId)
            stopAdvertising()
        }
    }

    override fun onConnected(endpointId: String, info: ClientInfo) {
        Timber.d("Connected: $endpointId")
        clientConnected(info)
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

        // TODO: add reconnection logic
        // if (left.isEmpty() xor right.isEmpty()) advertise()
    }

    private fun emmitClients() {
        onClientSetChangeListener!!(
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

    override fun onClientDisconnected(data: MeshData.ClientDisconnected) {
        Timber.d("Client disconnected: $data")
        clientDisconnected(data.clientInfo)
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
                        )
                    }
                    .also(::instance::set)
            }
    }
}