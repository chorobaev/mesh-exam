package io.flaterlab.meshexam.domain.create.usecase

import io.flaterlab.meshexam.domain.create.model.CreateAnswerModel
import io.flaterlab.meshexam.domain.repository.ExamRepository
import javax.inject.Inject

class CreateAnswerUseCase @Inject constructor(
    private val examRepository: ExamRepository,
) {

    suspend operator fun invoke(model: CreateAnswerModel): String {
        return examRepository.createAnswer(model)
    }
}