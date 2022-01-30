package io.flaterlab.meshexam.domain.usecase

import io.flaterlab.meshexam.domain.datasource.ExamDataSource
import io.flaterlab.meshexam.domain.model.ExamWithQuestionIdsModel
import javax.inject.Inject

class GetExamUseCase @Inject constructor(
    private val examDataSource: ExamDataSource,
) {

    suspend operator fun invoke(examId: String): ExamWithQuestionIdsModel {
        return examDataSource.getExamWithQuestionIdsByExamId(examId)
    }
}