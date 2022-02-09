package io.flaterlab.meshexam.domain.create.usecase

import io.flaterlab.meshexam.domain.create.model.QuestionModel
import io.flaterlab.meshexam.domain.repository.ExamRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetQuestionUseCase @Inject constructor(
    private val examRepository: ExamRepository,
) {

    operator fun invoke(questionId: String): Flow<QuestionModel> {
        return examRepository.questionById(questionId)
    }
}