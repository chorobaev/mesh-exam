package io.flaterlab.meshexam.domain.impl

import io.flaterlab.meshexam.domain.interactor.MeshInteractor
import io.flaterlab.meshexam.domain.repository.MeshRepository
import javax.inject.Inject

class MeshInteractorImpl @Inject constructor(
    private val meshRepository: MeshRepository,
) : MeshInteractor {

    override fun stopMesh() = meshRepository.stopMesh()
}