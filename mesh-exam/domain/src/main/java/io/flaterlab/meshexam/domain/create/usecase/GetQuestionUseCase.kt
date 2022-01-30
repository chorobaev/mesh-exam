package io.flaterlab.meshexam.domain.create.usecase

import io.flaterlab.meshexam.domain.create.model.QuestionModel
import io.flaterlab.meshexam.domain.datasource.ExamDataSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetQuestionUseCase @Inject constructor(
    private val examDataSource: ExamDataSource,
) {

    operator fun invoke(questionId: String): Flow<QuestionModel> {
        return examDataSource.questionById(questionId)
    }
}