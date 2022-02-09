package io.flaterlab.meshexam.domain.mesh.usecase

import io.flaterlab.meshexam.domain.repository.MeshRepository
import javax.inject.Inject

class RemoveClientUseCase @Inject constructor(
    private val meshRepository: MeshRepository,
) {

    suspend operator fun invoke(clientId: String) {

    }
}