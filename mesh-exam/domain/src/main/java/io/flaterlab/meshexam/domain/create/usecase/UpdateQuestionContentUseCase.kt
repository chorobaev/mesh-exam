package io.flaterlab.meshexam.domain.create.usecase

import io.flaterlab.meshexam.domain.datasource.ExamDataSource
import javax.inject.Inject

class UpdateQuestionContentUseCase @Inject constructor(
    private val examDataSource: ExamDataSource,
) {

    suspend operator fun invoke(questionId: String, content: String) {
        examDataSource.updateQuestionContent(questionId, content)
    }
}