package io.flaterlab.meshexam.domain.create.usecase

import io.flaterlab.meshexam.domain.create.model.CreateAnswerModel
import io.flaterlab.meshexam.domain.datasource.ExamDataSource
import javax.inject.Inject

class CreateAnswerUseCase @Inject constructor(
    private val examDataSource: ExamDataSource,
) {

    suspend operator fun invoke(model: CreateAnswerModel): String {
        return examDataSource.createAnswer(model)
    }
}