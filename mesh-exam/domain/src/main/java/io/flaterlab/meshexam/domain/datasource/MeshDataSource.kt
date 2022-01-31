package io.flaterlab.meshexam.domain.datasource

import io.flaterlab.meshexam.domain.mesh.model.MeshModel
import kotlinx.coroutines.flow.Flow

interface MeshDataSource {

    fun startMesh(examId: String): Flow<MeshModel>

    suspend fun removeClient(clientId: String)
}