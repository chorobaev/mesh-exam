package io.flaterlab.meshexam.domain.impl

import io.flaterlab.meshexam.domain.interactor.ExaminationInteractor
import io.flaterlab.meshexam.domain.repository.MeshRepository
import javax.inject.Inject

class ExaminationInteractorImpl @Inject constructor(
    private val meshRepository: MeshRepository,
) : ExaminationInteractor {

    override suspend fun joinExam(examId: String) {
        meshRepository.joinExam(examId)
    }
}