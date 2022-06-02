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
    private var lastMessage: Message? = null
    private var connectedEndpointId: String? = null

    suspend fun sendMessage(message: Message) {
        Timber.d("Starting sending payload: $message")
        this.lastMessage = message
        discover()
        try {
            CompletableDeferred<Unit>()
                .also { onSendingResult = it }
                .await()
        } finally {
            onSendingResult = null
            this.lastMessage = null
            stopDiscovery()
            disconnect()
        }
    }

    private fun discover() {
        Timber.d("Starting discovering receivers...")
        val options = DiscoveryOptions.Builder()
            .setStrategy(Strategy.P2P_CLUSTER)
            .setLowPower(false)
            .build()
        nearby.startDiscovery(serviceId, receiverDiscoveryCallback, options)
            .addOnFailureListener { e ->
                onSendingResult?.completeExceptionally(e)
            }
    }

    private fun stopDiscovery() {
        Timber.d("Stopping discovering endpoints...")
        nearby.stopDiscovery()
    }

    private fun disconnect() {
        try {
            connectedEndpointId?.let { endpointId ->
                nearby.disconnectFromEndpoint(endpointId)
                Timber.d("Disconnected from endpoint, endpointId = $endpointId")
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    inner class ReceiverDiscoveryCallback : EndpointDiscoveryCallback() {

        override fun onEndpointFound(endpointId: String, info: DiscoveredEndpointInfo) {
            Timber.d("Endpoint found: $endpointId")
            val json = String(info.endpointInfo)
            val receiverInfo = gson.fromJson(json, ReceiverInfo::class.java)
            if (receiverInfo.uid == lastMessage?.receiverId) connect(endpointId)
        }

        private fun connect(endpointId: String) {
            Timber.d("Connecting endpoint = $endpointId, sender info = ${lastMessage?.senderInfo}")
            val senderInfo = gson.toJson(lastMessage!!.senderInfo).toByteArray()
            nearby.requestConnection(senderInfo, endpointId, senderConnectionLifecycle)
                .addOnFailureListener { e ->
                    Timber.e(
                        e,
                        "Connection failed, endpoint = $endpointId, sender info = ${lastMessage?.senderInfo}"
                    )
                    // TODO: do not compete if endpoint not found try again
                    onSendingResult?.completeExceptionally(e)
                }
        }

        override fun onEndpointLost(endpointId: String) {
            Timber.d("Endpoint lost: $endpointId")
        }
    }

    inner class SenderConnectionLifecycle : ConnectionLifecycleCallback() {

        override fun onConnectionInitiated(endpointId: String, info: ConnectionInfo) {
            Timber.d("Accepting connection, endpointId = $endpointId")
            nearby.acceptConnection(endpointId, senderPayloadCallback)
                .addOnFailureListener { e ->
                    Timber.e(e, "Accepting connection failed, endpointId = $endpointId")
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
            connectedEndpointId = endpointId
            val message = lastMessage
            if (message == null) {
                onSendingResult?.completeExceptionally(IllegalStateException("Endpoint lost"))
            }
            val bytes = gson.toJson(message).toByteArray()
            nearby.sendPayload(endpointId, Payload.fromBytes(bytes))
                .addOnSuccessListener {
                    Timber.d("Payload sent...")
                    onSendingResult?.complete(Unit)
                }
                .addOnFailureListener { e ->
                    Timber.e(e, "Payload sending error...")
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
            connectedEndpointId = null
        }
    }

    inner class SenderPayloadCallback : PayloadCallback() {

        override fun onPayloadReceived(p0: String, p1: Payload) {}

        override fun onPayloadTransferUpdate(endpointId: String, update: PayloadTransferUpdate) {}
    }
}