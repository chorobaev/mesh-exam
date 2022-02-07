package io.flaterlab.meshexam.librariy.mesh.client

import android.content.Context
import io.flaterlab.meshexam.librariy.mesh.client.exception.MeshConnectionException
import io.flaterlab.meshexam.librariy.mesh.common.dto.AdvertiserInfo
import io.flaterlab.meshexam.librariy.mesh.common.dto.ClientInfo
import io.flaterlab.meshexam.librariy.mesh.common.dto.ClientMesh
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import timber.log.Timber
import java.util.concurrent.atomic.AtomicInteger

class ClientMeshManager internal constructor(
    private val discoveryMesh: ClientDiscoveryMeshManager,
    private val advertisingMesh: ClientAdvertisingMeshManager,
) {
    private val coroutineScope = CoroutineScope(Dispatchers.Default + SupervisorJob())
    private val reconnectTryCount = AtomicInteger()

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
            advertisingMesh.disconnect()
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
            if (reconnectTryCount.getAndIncrement() >= RECONNECT_TRY_COUNT) {
                reconnectTryCount.set(0)
                throw MeshConnectionException(e)
            } else {
                delay(RECONNECT_WAITING_TIME_MS)
                joinExam(examId, clientInfo)
            }
        }
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
            ).also { instance = it }
        }
    }
}