package io.flaterlab.meshexam.domain.usecase

import io.flaterlab.meshexam.domain.datasource.ExamDataSource
import io.flaterlab.meshexam.domain.model.CreateQuestionModel
import javax.inject.Inject

class CreateQuestionUseCase @Inject constructor(
    private val examDataSource: ExamDataSource,
) {

    suspend operator fun invoke(model: CreateQuestionModel): String {
        return examDataSource.createQuestion(model)
    }
}