package io.flaterlab.meshexam.domain.create.usecase

import io.flaterlab.meshexam.domain.repository.ExamRepository
import javax.inject.Inject

class UpdateAnswerContentUseCase @Inject constructor(
    private val examRepository: ExamRepository,
) {

    suspend operator fun invoke(answerId: String, content: String) {
        examRepository.updateAnswerContent(answerId, content)
    }
}