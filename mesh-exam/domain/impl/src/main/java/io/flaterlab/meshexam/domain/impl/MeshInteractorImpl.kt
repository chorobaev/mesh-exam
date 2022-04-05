package io.flaterlab.meshexam.domain.impl

import io.flaterlab.meshexam.domain.exam.model.ExamEventModel
import io.flaterlab.meshexam.domain.interactor.MeshInteractor
import io.flaterlab.meshexam.domain.mesh.model.HostedStudentModel
import io.flaterlab.meshexam.domain.mesh.model.MeshModel
import io.flaterlab.meshexam.domain.mesh.model.StartExamResultModel
import io.flaterlab.meshexam.domain.repository.ExamRepository
import io.flaterlab.meshexam.domain.repository.MeshRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MeshInteractorImpl @Inject constructor(
    private val meshRepository: MeshRepository,
    private val examRepository: ExamRepository,
) : MeshInteractor {

    override fun creteMesh(examId: String): Flow<MeshModel> = meshRepository.createMesh(examId)

    override suspend fun destroyMesh(examId: String) = meshRepository.destroyMesh(examId)

    override suspend fun destroyMeshByHostingId(hostingId: String) {
        meshRepository.destroyMesh(examRepository.getExamByHostingId(hostingId).id)
    }

    override suspend fun removeClient(clientId: String) = meshRepository.removeClient(clientId)

    override suspend fun finishExam(hostingId: String) = meshRepository.finishExam(hostingId)

    override suspend fun startExam(examId: String): StartExamResultModel {
        return meshRepository.startExam(examId)
    }

    override fun hostingTimeLeftInSec(hostingId: String): Flow<Int> {
        return meshRepository.hostingTimeLeftInSec(hostingId)
    }

    override fun hostedStudentList(
        hostingId: String,
        searchText: String?,
    ): Flow<List<HostedStudentModel>> {
        return meshRepository.hostedStudentList(hostingId, searchText)
    }

    override fun hostingEventList(hostingId: String): Flow<List<ExamEventModel>> {
        return meshRepository.examEvents(hostingId)
    }
}