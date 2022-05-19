package io.flaterlab.meshexam.library.messaging

import com.google.android.gms.nearby.connection.*
import com.google.gson.Gson
import kotlinx.coroutines.CompletableDeferred
import timber.log.Timber

internal class Sender(
    private val serviceId: String,
    private val nearby: ConnectionsClient,
    private val gson: Gson,
) {

    private val receiverDiscoveryCallback = ReceiverDiscoveryCallback()
    private val senderConnectionLifecycle = SenderConnectionLifecycle()
    private val senderPayloadCallback = SenderPayloadCallback()

    private var onSendingResult: CompletableDeferred<Unit>? = null
    private var payload: Message.Payload? = null

    suspend fun sendMessage(payload: Message.Payload) {
        this.payload = payload
        discover()
        try {
            CompletableDeferred<Unit>()
                .also { onSendingResult = it }
                .await()
        } finally {
            onSendingResult = null
            this.payload = null
            stopDiscovery()
        }
    }

    private fun discover() {
        val options = DiscoveryOptions.Builder()
            .setStrategy(Strategy.P2P_STAR)
            .setLowPower(false)
            .build()
        nearby.startDiscovery(serviceId, receiverDiscoveryCallback, options)
            .addOnFailureListener { e ->
                onSendingResult?.completeExceptionally(e)
            }
    }

    private fun stopDiscovery() {
        nearby.stopDiscovery()
    }

    inner class ReceiverDiscoveryCallback : EndpointDiscoveryCallback() {

        override fun onEndpointFound(endpointId: String, info: DiscoveredEndpointInfo) {
            Timber.d("Endpoint found: $endpointId")
            val json = String(info.endpointInfo)
            val receiverInfo = gson.fromJson(json, ReceiverInfo::class.java)
            if (receiverInfo.uid == payload?.receiverId) connect(endpointId)
        }

        private fun connect(endpointId: String) {
            val senderInfo = gson.toJson(payload!!.senderInfo).toByteArray()
            nearby.requestConnection(senderInfo, endpointId, senderConnectionLifecycle)
                .addOnFailureListener { e ->
                    onSendingResult?.completeExceptionally(e)
                }
        }

        override fun onEndpointLost(endpointId: String) {
            Timber.d("Endpoint lost: $endpointId")
        }
    }

    inner class SenderConnectionLifecycle : ConnectionLifecycleCallback() {

        override fun onConnectionInitiated(endpointId: String, info: ConnectionInfo) {
            nearby.acceptConnection(endpointId, senderPayloadCallback)
                .addOnFailureListener { e ->
                    onSendingResult?.completeExceptionally(e)
                }
        }

        override fun onConnectionResult(endpointId: String, resolution: ConnectionResolution) {
            when (resolution.status.statusCode) {
                ConnectionsStatusCodes.STATUS_OK ->
                    onConnected(endpointId)
                ConnectionsStatusCodes.STATUS_CONNECTION_REJECTED ->
                    onConnectionRejected(endpointId, Exception(resolution.status.statusMessage))
                ConnectionsStatusCodes.STATUS_ERROR ->
                    onConnectionError(endpointId, Exception(resolution.status.statusMessage))
            }
        }

        private fun onConnected(endpointId: String) {
            Timber.d("Connected: $endpointId")
            val bytes = gson.toJson(payload!!).toByteArray()
            nearby.sendPayload(endpointId, Payload.fromBytes(bytes))
                .addOnSuccessListener {
                    onSendingResult?.complete(Unit)
                }
                .addOnFailureListener { e ->
                    onSendingResult?.completeExceptionally(e)
                }
        }

        private fun onConnectionRejected(endpointId: String, cause: Throwable) {
            Timber.d("Connection rejected: $endpointId")
        }

        private fun onConnectionError(endpointId: String, cause: Throwable) {
            Timber.d("Connection error: $endpointId")
        }

        override fun onDisconnected(endpointId: String) {
            Timber.d("Disconnected: $endpointId")
        }
    }

    inner class SenderPayloadCallback : PayloadCallback() {

        override fun onPayloadReceived(p0: String, p1: Payload) {}

        override fun onPayloadTransferUpdate(endpointId: String, update: PayloadTransferUpdate) {}
    }
}