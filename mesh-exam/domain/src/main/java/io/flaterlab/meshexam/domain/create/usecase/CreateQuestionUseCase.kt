package io.flaterlab.meshexam.domain.create.usecase

import io.flaterlab.meshexam.domain.repository.ExamRepository
import io.flaterlab.meshexam.domain.create.model.CreateQuestionModel
import javax.inject.Inject

class CreateQuestionUseCase @Inject constructor(
    private val examRepository: ExamRepository,
) {

    suspend operator fun invoke(model: CreateQuestionModel): String {
        return examRepository.createQuestion(model)
    }
}