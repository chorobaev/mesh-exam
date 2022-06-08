package io.flaterlab.meshexam.domain.impl.mock

import io.flaterlab.meshexam.domain.exam.model.ExamEventModel
import io.flaterlab.meshexam.domain.interactor.MeshInteractor
import io.flaterlab.meshexam.domain.mesh.model.*
import io.flaterlab.meshexam.domain.repository.MeshRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
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

    override fun hostingState(hostingId: String): Flow<HostingMetaModel> {
        return emptyFlow()
    }

    override fun hostingTimeLeftInSec(hostingId: String): Flow<Int> {
        return emptyFlow()
    }

    override fun hostedStudentList(
        hostingId: String,
        searchText: String?
    ): Flow<List<HostedStudentModel>> {
        return emptyFlow()
    }

    override fun hostingEventList(hostingId: String): Flow<List<ExamEventModel>> {
        return emptyFlow()
    }

    override suspend fun destroyMeshByHostingId(hostingId: String) {
        println("Destroying mesh")
    }
}