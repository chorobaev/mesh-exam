package io.flaterlab.meshexam.domain.create.usecase

import io.flaterlab.meshexam.domain.create.model.AnswerModel
import io.flaterlab.meshexam.domain.repository.ExamRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAnswersUseCase @Inject constructor(
    private val examRepository: ExamRepository,
) {

    operator fun invoke(questionId: String): Flow<List<AnswerModel>> {
        return examRepository.answersByQuestionId(questionId)
    }
}