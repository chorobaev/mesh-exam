package io.flaterlab.meshexam.domain.create.usecase

import io.flaterlab.meshexam.domain.repository.ExamRepository
import javax.inject.Inject

class UpdateAnswerCorrectnessUseCase @Inject constructor(
    private val examRepository: ExamRepository,
) {

    suspend operator fun invoke(answerId: String, isCorrect: Boolean) {
        examRepository.updateAnswerCorrectness(answerId, isCorrect)
    }
}