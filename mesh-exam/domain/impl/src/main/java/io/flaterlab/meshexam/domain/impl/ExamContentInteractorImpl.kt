package io.flaterlab.meshexam.domain.impl

import io.flaterlab.meshexam.domain.create.model.ExamModel
import io.flaterlab.meshexam.domain.interactor.ExamContentInteractor
import io.flaterlab.meshexam.domain.repository.ExamRepository
import javax.inject.Inject

class ExamContentInteractorImpl @Inject constructor(
    private val examRepository: ExamRepository,
) : ExamContentInteractor {

    override suspend fun deleteExamById(examId: String) {
        examRepository.deleteExamById(examId)
    }

    override suspend fun updateExam(model: ExamModel) {
        examRepository.updateExam(model)
    }
}