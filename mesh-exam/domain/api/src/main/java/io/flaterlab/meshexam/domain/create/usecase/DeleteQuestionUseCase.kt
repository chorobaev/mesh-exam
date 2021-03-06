package io.flaterlab.meshexam.domain.create.usecase

import io.flaterlab.meshexam.domain.repository.ExamRepository
import javax.inject.Inject

class DeleteQuestionUseCase @Inject constructor(
    private val examRepository: ExamRepository,
) {

    suspend operator fun invoke(vararg questionIds: String) {
        examRepository.deleteQuestions(*questionIds)
    }
}