package io.flaterlab.meshexam.data.datasource

import io.flaterlab.meshexam.domain.datasource.MeshDataSource
import io.flaterlab.meshexam.domain.mesh.model.ClientModel
import io.flaterlab.meshexam.domain.mesh.model.MeshModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import java.util.*
import javax.inject.Inject

internal class MeshDataSourceImpl @Inject constructor(

) : MeshDataSource {

    override fun startMesh(examId: String): Flow<MeshModel> {
        // TODO: add implementation
        return flowOf(
            MeshModel(
                (1..15).map {
                    ClientModel(
                        UUID.randomUUID().toString(),
                        "Nurbol Chorobaev $it",
                        "COM-18",
                        status = "status $it"
                    )
                }
            )
        )
    }

    override suspend fun removeClient(clientId: String) {
        // TODO: implement removal
    }
}