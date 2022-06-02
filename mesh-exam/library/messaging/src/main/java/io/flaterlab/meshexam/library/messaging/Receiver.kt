package io.flaterlab.meshexam.library.messaging

import com.google.android.gms.nearby.connection.*
import com.google.gson.Gson
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.isActive
import timber.log.Timber

internal class Receiver(
    private val serviceId: String,
    private val nearby: ConnectionsClient,
    private val gson: Gson,
) {

    private val receiverConnectionLifecycle = ReceiverConnectionLifecycle()
    private val receiverPayloadCallback = ReceiverPayloadCallback()
    private val connectedEndpointIds: MutableSet<String> = HashSet()

    private var onMessageReceiver: ((Result<Message>) -> Unit)? = null

    fun startReceiving(info: ReceiverInfo): Flow<Message> = callbackFlow<Message> {
        onMessageReceiver = { result: Result<Message> ->
            if (isActive) result.onSuccess(::trySend).onFailure(::close)
        }
        advertise(info)
        awaitClose {
            onMessageReceiver = null
            stopAdvertising()
            disconnect()
        }
    }.also {
        Timber.d("Starting receiving, receiver info = $info")
    }.onStart {
        Timber.d("Receiving started...")
    }.onCompletion {
        Timber.d("Receiving competed...")
    }.onEach {
        Timber.d("Received message = $it")
    }

    private fun advertise(receiverInfo: ReceiverInfo) {
        Timber.d("Starting advertising")
        val infoBytes = gson.toJson(receiverInfo).toByteArray()
        val options = AdvertisingOptions.Builder()
            .setStrategy(Strategy.P2P_CLUSTER)
            .setLowPower(false)
            .setDisruptiveUpgrade(false)
            .build()
        nearby.startAdvertising(infoBytes, serviceId, receiverConnectionLifecycle, options)
            .addOnSuccessListener {
                Timber.d("Messaging advertising started successfully...")
            }
            .addOnFailureListener { e ->
                onMessageReceiver?.invoke(Result.failure(e))
            }
    }

    private fun stopAdvertising() {
        nearby.stopAdvertising()
    }

    private fun disconnect() {
        connectedEndpointIds.forEach(nearby::disconnectFromEndpoint)
        connectedEndpointIds.clear()
    }

    inner class ReceiverConnectionLifecycle : ConnectionLifecycleCallback() {

        override fun onConnectionInitiated(endpointId: String, info: ConnectionInfo) {
            val result = runCatching {
                val json = String(info.endpointInfo)
                gson.fromJson(json, SenderInfo::class.java)
            }
            Timber.d("Connection requested, result = $result")
            result
                .onSuccess {
                    nearby.acceptConnection(endpointId, receiverPayloadCallback)
                        .addOnFailureListener { e ->
                            onMessageReceiver?.invoke(Result.failure(e))
                        }
                }
                .onFailure { e ->
                    onMessageReceiver?.invoke(Result.failure(e))
                }
        }

        override fun onConnectionResult(endpointId: String, resolution: ConnectionResolution) {
            when (resolution.status.statusCode) {
                ConnectionsStatusCodes.STATUS_OK ->
                    onConnected(endpointId)
                ConnectionsStatusCodes.STATUS_CONNECTION_REJECTED ->
                    Timber.d("Connection rejected: $endpointId")
                ConnectionsStatusCodes.STATUS_ERROR ->
                    Timber.d("Connection error: $endpointId")
            }
        }

        private fun onConnected(endpointId: String) {
            Timber.d("Connected: $endpointId")
            connectedEndpointIds.add(endpointId)
        }

        override fun onDisconnected(endpointId: String) {
            Timber.d("Disconnected: $endpointId")
            connectedEndpointIds.remove(endpointId)
        }
    }

    inner class ReceiverPayloadCallback : PayloadCallback() {

        override fun onPayloadReceived(endpointId: String, payload: Payload) {
            when (payload.type) {
                Payload.Type.BYTES -> payload.asBytes()?.let(::onBytesReceived)
            }
        }

        private fun onBytesReceived(bytes: ByteArray) {
            val message = runCatching {
                String(bytes)
            }.mapCatching { json ->
                gson.fromJson(json, Message::class.java)
            }
            Timber.d("On message received, message = $message")
            onMessageReceiver?.invoke(message)
        }

        override fun onPayloadTransferUpdate(endpointId: String, update: PayloadTransferUpdate) {}
    }
}