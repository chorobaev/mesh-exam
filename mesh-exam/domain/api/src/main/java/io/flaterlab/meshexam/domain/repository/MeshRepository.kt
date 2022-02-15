package io.flaterlab.meshexam.domain.repository

import io.flaterlab.meshexam.domain.mesh.model.MeshModel
import kotlinx.coroutines.flow.Flow

interface MeshRepository {

    fun startMesh(examId: String): Flow<MeshModel>

    fun stopMesh()

    suspend fun removeClient(clientId: String)
}