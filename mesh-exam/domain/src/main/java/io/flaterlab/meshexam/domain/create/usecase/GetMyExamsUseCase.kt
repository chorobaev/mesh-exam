package io.flaterlab.meshexam.domain.create.usecase

import io.flaterlab.meshexam.domain.datasource.ExamDataSource
import io.flaterlab.meshexam.domain.api.model.ExamModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetMyExamsUseCase @Inject constructor(
    private val examDataSource: ExamDataSource,
) {

    operator fun invoke(): Flow<List<ExamModel>> {
        return examDataSource.exams()
    }
}