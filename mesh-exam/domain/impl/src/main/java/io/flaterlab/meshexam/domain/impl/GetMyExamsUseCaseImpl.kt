package io.flaterlab.meshexam.domain.impl

import io.flaterlab.meshexam.domain.api.datasource.ExamDataSource
import io.flaterlab.meshexam.domain.api.model.ExamModel
import io.flaterlab.meshexam.domain.api.usecase.GetMyExamUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetMyExamsUseCaseImpl @Inject constructor(
    private val examDataSource: ExamDataSource,
) : GetMyExamUseCase {

    override fun invoke(): Flow<List<ExamModel>> {
        return examDataSource.exams()
    }
}