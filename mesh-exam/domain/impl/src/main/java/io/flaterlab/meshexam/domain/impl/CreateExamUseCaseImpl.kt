package io.flaterlab.meshexam.domain.impl

import io.flaterlab.meshexam.domain.api.datasource.ExamDataSource
import io.flaterlab.meshexam.domain.api.model.CreateExamModel
import io.flaterlab.meshexam.domain.api.usecase.CreateExamUseCase
import javax.inject.Inject

class CreateExamUseCaseImpl @Inject constructor(
    private val examDataSource: ExamDataSource,
) : CreateExamUseCase {

    override suspend fun invoke(modelCreate: CreateExamModel): String {
        return examDataSource.createTest(modelCreate)
    }
}