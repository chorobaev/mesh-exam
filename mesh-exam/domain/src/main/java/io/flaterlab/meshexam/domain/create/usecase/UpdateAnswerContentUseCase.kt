package io.flaterlab.meshexam.domain.create.usecase

import io.flaterlab.meshexam.domain.datasource.ExamDataSource
import javax.inject.Inject

class UpdateAnswerContentUseCase @Inject constructor(
    private val examDataSource: ExamDataSource,
) {

    suspend operator fun invoke(answerId: String, content: String) {
        examDataSource.updateAnswerContent(answerId, content)
    }
}