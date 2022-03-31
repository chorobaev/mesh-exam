package io.flaterlab.meshexam.domain.repository

import io.flaterlab.meshexam.domain.mesh.model.HostedStudentModel
import io.flaterlab.meshexam.domain.mesh.model.MeshModel
import io.flaterlab.meshexam.domain.mesh.model.StartExamResultModel
import kotlinx.coroutines.flow.Flow

interface MeshRepository {

    fun createMesh(examId: String): Flow<MeshModel>

    suspend fun destroyMesh(examId: String)

    suspend fun removeClient(clientId: String)

    suspend fun startExam(examId: String): StartExamResultModel

    suspend fun finishExam(hostingId: String)

    fun hostingTimeLeftInSec(hostingId: String): Flow<Int>

    fun hostedStudentList(
        hostingId: String,
        searchText: String?,
    ): Flow<List<HostedStudentModel>>
}