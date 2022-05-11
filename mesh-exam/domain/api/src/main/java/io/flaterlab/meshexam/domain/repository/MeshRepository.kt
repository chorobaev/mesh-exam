package io.flaterlab.meshexam.domain.repository

import io.flaterlab.meshexam.domain.exam.model.ExamEventModel
import io.flaterlab.meshexam.domain.mesh.model.HostedStudentModel
import io.flaterlab.meshexam.domain.mesh.model.HostingMetaModel
import io.flaterlab.meshexam.domain.mesh.model.MeshModel
import io.flaterlab.meshexam.domain.mesh.model.StartExamResultModel
import kotlinx.coroutines.flow.Flow

interface MeshRepository {

    fun createMesh(examId: String): Flow<MeshModel>

    suspend fun destroyMesh(examId: String)

    suspend fun removeClient(clientId: String)

    suspend fun startExam(examId: String): StartExamResultModel

    suspend fun finishExam(hostingId: String, notifyClientsToFinish: Boolean = true)

    fun hostingMetaModel(hostingId: String): Flow<HostingMetaModel>

    fun hostingTimeLeftInSec(hostingId: String): Flow<Int>

    fun hostedStudentList(
        hostingId: String,
        searchText: String?,
    ): Flow<List<HostedStudentModel>>

    fun examEvents(hostingId: String): Flow<List<ExamEventModel>>
}