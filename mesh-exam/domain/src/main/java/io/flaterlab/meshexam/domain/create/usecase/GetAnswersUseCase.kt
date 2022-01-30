package io.flaterlab.meshexam.domain.create.usecase

import io.flaterlab.meshexam.domain.create.model.AnswerModel
import io.flaterlab.meshexam.domain.datasource.ExamDataSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAnswersUseCase @Inject constructor(
    private val examDataSource: ExamDataSource,
) {

    operator fun invoke(questionId: String): Flow<List<AnswerModel>> {
        return examDataSource.answersByQuestionId(questionId)
    }
}