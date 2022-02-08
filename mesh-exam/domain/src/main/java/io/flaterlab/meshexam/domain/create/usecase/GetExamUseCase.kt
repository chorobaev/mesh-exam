package io.flaterlab.meshexam.domain.create.usecase

import io.flaterlab.meshexam.domain.repository.ExamRepository
import io.flaterlab.meshexam.domain.create.model.ExamWithQuestionIdsModel
import javax.inject.Inject

class GetExamUseCase @Inject constructor(
    private val examRepository: ExamRepository,
) {

    suspend operator fun invoke(examId: String): ExamWithQuestionIdsModel {
        return examRepository.getExamWithQuestionIdsByExamId(examId)
    }
}