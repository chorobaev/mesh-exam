package io.flaterlab.meshexam.domain.interactor

import io.flaterlab.meshexam.domain.mesh.model.MeshModel
import io.flaterlab.meshexam.domain.mesh.model.StartExamResultModel
import kotlinx.coroutines.flow.Flow

interface MeshInteractor {

    fun creteMesh(examId: String): Flow<MeshModel>

    suspend fun removeClient(clientId: String)

    fun stopMesh()

    suspend fun startExam(examId: String): StartExamResultModel

    fun hostingTimeLeftInSec(hostingId: String): Flow<Int>
}