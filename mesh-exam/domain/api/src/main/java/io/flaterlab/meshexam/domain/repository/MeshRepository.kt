package io.flaterlab.meshexam.domain.repository

import io.flaterlab.meshexam.domain.mesh.model.MeshModel
import io.flaterlab.meshexam.domain.mesh.model.StartExamResultModel
import kotlinx.coroutines.flow.Flow

interface MeshRepository {

    fun createMesh(examId: String): Flow<MeshModel>

    fun stopMesh()

    suspend fun removeClient(clientId: String)

    suspend fun startExam(examId: String): StartExamResultModel
}