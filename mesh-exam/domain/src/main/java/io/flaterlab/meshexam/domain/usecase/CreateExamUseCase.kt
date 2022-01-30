package io.flaterlab.meshexam.domain.usecase

import io.flaterlab.meshexam.domain.datasource.ExamDataSource
import io.flaterlab.meshexam.domain.api.model.CreateExamModel
import javax.inject.Inject

class CreateExamUseCase @Inject constructor(
    private val examDataSource: ExamDataSource,
) {

    suspend operator fun invoke(modelCreate: CreateExamModel): String {
        return examDataSource.createExam(modelCreate)
    }
}