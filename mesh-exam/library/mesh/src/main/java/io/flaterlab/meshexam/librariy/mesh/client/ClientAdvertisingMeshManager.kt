package io.flaterlab.meshexam.librariy.mesh.client

import android.content.Context
import com.google.android.gms.nearby.Nearby
import com.google.android.gms.nearby.connection.AdvertisingOptions
import com.google.android.gms.nearby.connection.ConnectionsClient
import com.google.android.gms.nearby.connection.Strategy
import com.google.gson.GsonBuilder
import io.flaterlab.meshexam.librariy.mesh.common.ConnectionsLifecycleAdapterCallback2
import io.flaterlab.meshexam.librariy.mesh.common.PayloadAdapterCallback
import io.flaterlab.meshexam.librariy.mesh.common.dto.AdvertiserInfo
import io.flaterlab.meshexam.librariy.mesh.common.dto.ChildInfo
import io.flaterlab.meshexam.librariy.mesh.common.dto.ClientInfo
import io.flaterlab.meshexam.librariy.mesh.common.parser.AdvertiserInfoJsonParser
import io.flaterlab.meshexam.librariy.mesh.common.parser.ClientInfoJsonParser
import io.flaterlab.meshexam.librariy.mesh.common.parser.JsonParser
import timber.log.Timber

internal class ClientAdvertisingMeshManager(
    private val serviceId: String,
    private val nearby: ConnectionsClient,
    private val connectionsCallback: ConnectionsLifecycleAdapterCallback2<ClientInfo>,
    private val payloadCallback: PayloadAdapterCallback,
    private val advertiserInfoJsonParser: JsonParser<AdvertiserInfo>,
) : ConnectionsLifecycleAdapterCallback2.AdapterCallback<ClientInfo>,
    PayloadAdapterCallback.AdapterCallback {

    var onClientConnectedListener: (ClientInfo) -> Unit = {}
    var onClientDisconnectedListener: (ClientInfo) -> Unit = {}
    var onErrorListener: (Exception) -> Unit = {}

    private var advertiserInfo: AdvertiserInfo? = null
    private var child: ChildInfo? = null

    init {
        connectionsCallback.adapterCallback = this
        payloadCallback.adapterCallback = this
    }

    fun advertise(info: AdvertiserInfo) {
        advertiserInfo = info
        startAdvertising()
    }

    private fun startAdvertising() {
        val infoJson = advertiserInfoJsonParser
            .toJson(advertiserInfo!!)
            .toByteArray()
        val options = AdvertisingOptions.Builder()
            .setDisruptiveUpgrade(false)
            .setStrategy(Strategy.P2P_CLUSTER)
            .build()
        nearby.startAdvertising(infoJson, serviceId, connectionsCallback, options)
            .addOnFailureListener(::onError)
    }

    fun close() {
        nearby.stopAdvertising()
        child?.first?.let(nearby::disconnectFromEndpoint)
        advertiserInfo = null
        child = null
    }

    override fun onRequested(endpointId: String, info: ClientInfo) {
        nearby.acceptConnection(endpointId, payloadCallback)
            .addOnFailureListener(::onError)
    }

    override fun onConnected(endpointId: String, info: ClientInfo) {
        if (child == null) {
            child = endpointId to info
            onClientConnectedListener(info)
            nearby.stopAdvertising()
        } else {
            nearby.disconnectFromEndpoint(endpointId)
        }
    }

    override fun onRejected(endpointId: String, info: ClientInfo) = Unit

    override fun onError(endpointId: String, info: ClientInfo) = Unit

    override fun onDisconnected(endpointId: String, info: ClientInfo) {
        if (child?.first == endpointId) {
            child = null
            onClientDisconnectedListener(info)
            startAdvertising()
        }
    }

    override fun rejectConnection(endpointId: String) {
        nearby.rejectConnection(endpointId)
    }

    private fun onError(e: Exception) {
        close()
        onErrorListener(e)
        Timber.e(e)
    }

    companion object {
        fun getInstance(context: Context): ClientAdvertisingMeshManager =
            GsonBuilder()
                .excludeFieldsWithoutExposeAnnotation()
                .create()
                .let { gson ->
                    ClientAdvertisingMeshManager(
                        serviceId = context.packageName,
                        nearby = Nearby.getConnectionsClient(context),
                        connectionsCallback = ConnectionsLifecycleAdapterCallback2(
                            ClientInfoJsonParser(gson)
                        ),
                        payloadCallback = PayloadAdapterCallback(gson),
                        advertiserInfoJsonParser = AdvertiserInfoJsonParser(gson)
                    )
                }
    }
}