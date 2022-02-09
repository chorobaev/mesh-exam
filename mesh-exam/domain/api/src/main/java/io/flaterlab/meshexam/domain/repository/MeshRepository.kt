package io.flaterlab.meshexam.domain.repository

import io.flaterlab.meshexam.domain.mesh.model.MeshModel
import kotlinx.coroutines.flow.Flow

interface MeshRepository {

    fun startMesh(examId: String): Flow<MeshModel>

    suspend fun removeClient(clientId: String)

    suspend fun joinExam(examId: String)
}