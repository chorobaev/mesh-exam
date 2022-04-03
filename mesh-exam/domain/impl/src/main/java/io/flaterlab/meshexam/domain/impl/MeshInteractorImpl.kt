package io.flaterlab.meshexam.domain.impl

import io.flaterlab.meshexam.domain.exam.model.ExamEventModel
import io.flaterlab.meshexam.domain.interactor.MeshInteractor
import io.flaterlab.meshexam.domain.mesh.model.HostedStudentModel
import io.flaterlab.meshexam.domain.mesh.model.MeshModel
import io.flaterlab.meshexam.domain.mesh.model.StartExamResultModel
import io.flaterlab.meshexam.domain.repository.MeshRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MeshInteractorImpl @Inject constructor(
    private val meshRepository: MeshRepository,
) : MeshInteractor {

    override fun creteMesh(examId: String): Flow<MeshModel> = meshRepository.createMesh(examId)

    override suspend fun destroyMesh(examId: String) = meshRepository.destroyMesh(examId)

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

    override fun examEventList(hostingId: String): Flow<List<ExamEventModel>> {
        return meshRepository.examEvents(hostingId)
    }
}