package io.flaterlab.meshexam.domain.interactor

import io.flaterlab.meshexam.domain.exam.model.ExamEventModel
import io.flaterlab.meshexam.domain.mesh.model.HostedStudentModel
import io.flaterlab.meshexam.domain.mesh.model.HostingMetaModel
import io.flaterlab.meshexam.domain.mesh.model.MeshModel
import io.flaterlab.meshexam.domain.mesh.model.StartExamResultModel
import kotlinx.coroutines.flow.Flow

interface MeshInteractor {

    fun creteMesh(examId: String): Flow<MeshModel>

    suspend fun destroyMesh(examId: String)

    suspend fun destroyMeshByHostingId(hostingId: String)

    suspend fun removeClient(clientId: String)

    suspend fun finishExam(hostingId: String)

    suspend fun startExam(examId: String): StartExamResultModel

    fun hostingState(hostingId: String): Flow<HostingMetaModel>

    fun hostingTimeLeftInSec(hostingId: String): Flow<Int>

    fun hostedStudentList(
        hostingId: String,
        searchText: String? = null
    ): Flow<List<HostedStudentModel>>

    fun hostingEventList(hostingId: String): Flow<List<ExamEventModel>>
}