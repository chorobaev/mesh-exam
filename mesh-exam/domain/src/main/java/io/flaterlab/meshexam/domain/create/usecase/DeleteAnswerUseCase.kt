package io.flaterlab.meshexam.domain.create.usecase

import io.flaterlab.meshexam.domain.repository.ExamRepository
import javax.inject.Inject

class DeleteAnswerUseCase @Inject constructor(
    private val examRepository: ExamRepository,
) {

    suspend operator fun invoke(vararg answerIds: String) {
        examRepository.deleteAnswers(*answerIds)
    }
}