package io.flaterlab.meshexam.domain.impl

import io.flaterlab.meshexam.domain.interactor.MeshInteractor
import io.flaterlab.meshexam.domain.mesh.model.MeshModel
import io.flaterlab.meshexam.domain.mesh.model.StartExamResultModel
import io.flaterlab.meshexam.domain.repository.MeshRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MeshInteractorImpl @Inject constructor(
    private val meshRepository: MeshRepository,
) : MeshInteractor {

    override fun creteMesh(examId: String): Flow<MeshModel> = meshRepository.createMesh(examId)

    override suspend fun removeClient(clientId: String) = meshRepository.removeClient(clientId)

    override fun stopMesh() = meshRepository.stopMesh()

    override suspend fun startExam(examId: String): StartExamResultModel {
        return meshRepository.startExam(examId)
    }
}