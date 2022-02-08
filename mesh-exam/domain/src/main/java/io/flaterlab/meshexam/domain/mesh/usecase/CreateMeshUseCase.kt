package io.flaterlab.meshexam.domain.mesh.usecase

import io.flaterlab.meshexam.domain.repository.MeshRepository
import io.flaterlab.meshexam.domain.mesh.model.MeshModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CreateMeshUseCase @Inject constructor(
    private val meshRepository: MeshRepository,
) {

    operator fun invoke(examId: String): Flow<MeshModel> {
        return meshRepository.startMesh(examId)
    }
}