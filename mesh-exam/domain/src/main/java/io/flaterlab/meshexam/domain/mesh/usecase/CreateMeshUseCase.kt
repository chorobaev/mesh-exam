package io.flaterlab.meshexam.domain.mesh.usecase

import io.flaterlab.meshexam.domain.datasource.MeshDataSource
import io.flaterlab.meshexam.domain.mesh.model.MeshModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CreateMeshUseCase @Inject constructor(
    private val meshDataSource: MeshDataSource,
) {

    operator fun invoke(examId: String): Flow<MeshModel> {
        return meshDataSource.startMesh(examId)
    }
}