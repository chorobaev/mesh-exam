package io.flaterlab.meshexam.librariy.mesh.client

import android.content.Context
import com.google.android.gms.nearby.connection.Payload
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import io.flaterlab.meshexam.librariy.mesh.client.exception.MeshConnectionException
import io.flaterlab.meshexam.librariy.mesh.common.dto.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import timber.log.Timber
import java.util.concurrent.atomic.AtomicInteger

class ClientMeshManager internal constructor(
    private val discoveryMesh: ClientDiscoveryMeshManager,
    private val advertisingMesh: ClientAdvertisingMeshManager,
    private val gson: Gson,
) {

    private val coroutineScope = CoroutineScope(Dispatchers.Default + SupervisorJob())
    private val reconnectTryCount = AtomicInteger()

    var onPayloadFromHostCallback: (suspend (FromHostPayload) -> Unit)? = null

    init {
        advertisingMesh.onClientConnectedListener = { client ->
            coroutineScope.launch {
                discoveryMesh.notifyClientConnected(client)
            }
        }
        advertisingMesh.onClientDisconnectedListener = { client ->
            coroutineScope.launch {
                discoveryMesh.notifyClientDisconnected(client)
            }
        }
        advertisingMesh.onErrorListener = { e ->
            Timber.d(e)
        }
        advertisingMesh.setOnBytesReceivedListener { bytes ->
            coroutineScope.launch {
                discoveryMesh.forwardBytes(bytes)
            }
        }
        discoveryMesh.onDisconnectedListener = { _, _ ->
            advertisingMesh.close()
        }
        discoveryMesh.setOnBytesReceivedListener { bytes ->
            Timber.d("FromHostPayload received: ${String(bytes)}")
            coroutineScope.launch {
                try {
                    val payload = gson.fromJson(String(bytes), FromHostPayload::class.java)
                    onPayloadFromHostCallback?.invoke(payload)
                    advertisingMesh.forwardPayload(Payload.fromBytes(bytes))
                } catch (ex: Exception) {
                    Timber.e(ex, "FromHostPayload forwarding (last child)")
                }
            }
        }
    }

    fun discoverExams(): Flow<ClientMesh> =
        discoveryMesh.discoverExams()

    suspend fun joinExam(examId: String, clientInfo: ClientInfo): AdvertiserInfo {
        Timber.d("Trying to join: #${reconnectTryCount.get()}")
        return try {
            discoveryMesh.joinExam(examId, clientInfo)
                .also { advertisingMesh.advertise(it) }
        } catch (e: Exception) {
            Timber.e(e)
            if (reconnectTryCount.getAndIncrement() >= RECONNECT_TRY_COUNT) {
                reconnectTryCount.set(0)
                throw MeshConnectionException(e)
            } else {
                delay(RECONNECT_WAITING_TIME_MS)
                joinExam(examId, clientInfo)
            }
        }
    }

    fun leaveExam(examId: String) {
        Timber.d("Leaving exam: examId = $examId")
        discoveryMesh.leaveExam(examId)
    }

    suspend fun sendPayloadToHost(payload: FromClientPayload) {
        discoveryMesh.sendPayloadToHost(payload)
    }

    companion object {
        private const val RECONNECT_WAITING_TIME_MS = 1000L
        private const val RECONNECT_TRY_COUNT = 5

        @Volatile
        private var instance: ClientMeshManager? = null

        fun getInstance(context: Context): ClientMeshManager = instance ?: synchronized(this) {
            instance ?: ClientMeshManager(
                discoveryMesh = ClientDiscoveryMeshManager.getInstance(context),
                advertisingMesh = ClientAdvertisingMeshManager.getInstance(context),
                gson = GsonBuilder().excludeFieldsWithoutExposeAnnotation().create(),
            ).also { instance = it }
        }
    }
}