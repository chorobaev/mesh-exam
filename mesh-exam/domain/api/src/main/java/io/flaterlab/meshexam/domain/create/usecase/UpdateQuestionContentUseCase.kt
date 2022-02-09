package io.flaterlab.meshexam.domain.create.usecase

import io.flaterlab.meshexam.domain.repository.ExamRepository
import javax.inject.Inject

class UpdateQuestionContentUseCase @Inject constructor(
    private val examRepository: ExamRepository,
) {

    suspend operator fun invoke(questionId: String, content: String) {
        examRepository.updateQuestionContent(questionId, content)
    }
}