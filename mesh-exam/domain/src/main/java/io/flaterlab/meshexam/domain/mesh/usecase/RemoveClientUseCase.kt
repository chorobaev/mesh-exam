package io.flaterlab.meshexam.domain.mesh.usecase

import io.flaterlab.meshexam.domain.datasource.MeshDataSource
import javax.inject.Inject

class RemoveClientUseCase @Inject constructor(
    private val meshDataSource: MeshDataSource,
) {

    suspend operator fun invoke(clientId: String) {

    }
}