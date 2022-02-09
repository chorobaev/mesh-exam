package io.flaterlab.meshexam.domain.create.usecase

import io.flaterlab.meshexam.domain.repository.ExamRepository
import io.flaterlab.meshexam.domain.api.model.CreateExamModel
import javax.inject.Inject

class CreateExamUseCase @Inject constructor(
    private val examRepository: ExamRepository,
) {

    suspend operator fun invoke(modelCreate: CreateExamModel): String {
        return examRepository.createExam(modelCreate)
    }
}