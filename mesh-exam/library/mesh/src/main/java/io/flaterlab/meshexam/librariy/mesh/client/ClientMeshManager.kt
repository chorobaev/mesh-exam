package io.flaterlab.meshexam.librariy.mesh.client

import android.content.Context
import io.flaterlab.meshexam.librariy.mesh.common.dto.AdvertiserInfo
import io.flaterlab.meshexam.librariy.mesh.common.dto.ClientInfo
import io.flaterlab.meshexam.librariy.mesh.common.dto.ClientMesh
import io.flaterlab.meshexam.librariy.mesh.common.dto.MeshResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import timber.log.Timber

class ClientMeshManager internal constructor(
    private val discoveryMesh: ClientDiscoveryMeshManager,
    private val advertisingMesh: ClientAdvertisingMeshManager,
) {
    private val coroutineScope = CoroutineScope(Dispatchers.Default + SupervisorJob())

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
        discoveryMesh.onDisconnectedListener = { advertiserInfo, clientInfo ->
            coroutineScope.launch {
                joinExam(advertiserInfo.examId, clientInfo)
            }
        }
    }

    fun discoverExams(): Flow<MeshResult<ClientMesh>> =
        discoveryMesh.discoverExams()

    fun stopDiscovery() = discoveryMesh.stopDiscovery()

    suspend fun joinExam(examId: String, clientInfo: ClientInfo): AdvertiserInfo {
        return discoveryMesh.joinExam(examId, clientInfo)
            .also { advertisingMesh.advertise(it) }
    }

    companion object {
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