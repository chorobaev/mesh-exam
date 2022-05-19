package io.flaterlab.meshexam.library.messaging

import com.google.android.gms.nearby.connection.*
import com.google.gson.Gson
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.tasks.await
import timber.log.Timber

internal class Receiver(
    private val serviceId: String,
    private val nearby: ConnectionsClient,
    private val gson: Gson,
) {

    private val receiverConnectionLifecycle = ReceiverConnectionLifecycle()
    private val receiverPayloadCallback = ReceiverPayloadCallback()

    private var onMessageReceiver: ((Result<Message>) -> Unit)? = null

    fun startReceiving(info: ReceiverInfo): Flow<Message> = callbackFlow {
        onMessageReceiver = { result: Result<Message> ->
            if (isActive) result.onSuccess(::trySend).onFailure(::close)
        }
        advertise(info)
        awaitClose {
            onMessageReceiver = null
            stopAdvertising()
        }
    }

    private fun advertise(receiverInfo: ReceiverInfo) {
        val infoBytes = gson.toJson(receiverInfo).toByteArray()
        val options = AdvertisingOptions.Builder()
            .setStrategy(Strategy.P2P_STAR)
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

    suspend fun acceptMessage(request: Message.Request) {
        nearby.acceptConnection(request.endpointId, receiverPayloadCallback).await()
    }

    suspend fun rejectMessage(request: Message.Request) {
        nearby.rejectConnection(request.endpointId).await()
    }

    inner class ReceiverConnectionLifecycle : ConnectionLifecycleCallback() {

        override fun onConnectionInitiated(endpointId: String, info: ConnectionInfo) {
            val result = runCatching {
                val json = String(info.endpointInfo)
                val senderInfo = gson.fromJson(json, SenderInfo::class.java)
                Message.Request(
                    uid = senderInfo.uid,
                    endpointId = endpointId,
                )
            }
            onMessageReceiver?.invoke(result)
        }

        override fun onConnectionResult(endpointId: String, resolution: ConnectionResolution) {
            when (resolution.status.statusCode) {
                ConnectionsStatusCodes.STATUS_OK ->
                    Timber.d("Connected: $endpointId")
                ConnectionsStatusCodes.STATUS_CONNECTION_REJECTED ->
                    Timber.d("Connection rejected: $endpointId")
                ConnectionsStatusCodes.STATUS_ERROR ->
                    Timber.d("Connection error: $endpointId")
            }
        }

        override fun onDisconnected(endpointId: String) {
            Timber.d("Disconnected: $endpointId")
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
                gson.fromJson(json, Message.Payload::class.java)
            }
            onMessageReceiver?.invoke(message)
        }

        override fun onPayloadTransferUpdate(endpointId: String, update: PayloadTransferUpdate) {}
    }
}