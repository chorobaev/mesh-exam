package io.flaterlab.meshexam.domain.impl

import io.flaterlab.meshexam.domain.interactor.ContentInteractor
import io.flaterlab.meshexam.domain.repository.ExamRepository
import javax.inject.Inject

class ContentInteractorImpl @Inject constructor(
    private val examRepository: ExamRepository,
) : ContentInteractor {

    override suspend fun deleteExamById(examId: String) {
        examRepository.deleteExamById(examId)
    }
}