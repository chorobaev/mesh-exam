package io.flaterlab.meshexam.domain.impl.mock

import io.flaterlab.meshexam.domain.interactor.MeshInteractor
import io.flaterlab.meshexam.domain.mesh.model.ClientModel
import io.flaterlab.meshexam.domain.mesh.model.MeshModel
import io.flaterlab.meshexam.domain.mesh.model.StartExamResultModel
import io.flaterlab.meshexam.domain.repository.MeshRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

class MockMeshInteractor @Inject constructor(
    private val meshRepository: MeshRepository,
) : MeshInteractor {

    override fun creteMesh(examId: String): Flow<MeshModel> {
        return flowOf(
            MeshModel(
                clients = (1..10).map {
                    ClientModel(it.toString(), "Student #$it", "Group", it)
                }
            )
        )
    }

    override suspend fun destroyMesh(examId: String) {
        println("Destroying mesh")
    }

    override suspend fun removeClient(clientId: String) {
        println("Removing the client $clientId")
    }

    override suspend fun startExam(examId: String): StartExamResultModel {
        println("Starting the exam $examId")
        return meshRepository.startExam(examId)
    }

    override suspend fun finishExam(hostingId: String) {
        println("Stopping the mesh")
    }

    override fun hostingTimeLeftInSec(hostingId: String): Flow<Int> {
        TODO("Implement according to logic")
    }
}