package io.flaterlab.meshexam.domain.impl.mock

import io.flaterlab.meshexam.domain.interactor.MeshInteractor
import io.flaterlab.meshexam.domain.mesh.model.ClientModel
import io.flaterlab.meshexam.domain.mesh.model.MeshModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

class MockMeshInteractor @Inject constructor(

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

    override suspend fun removeClient(clientId: String) {
        println("Removing the client $clientId")
    }

    override suspend fun startExam(examId: String) {
        println("Starting the exam $examId")
    }

    override fun stopMesh() {
        println("Stopping the mesh")
    }
}