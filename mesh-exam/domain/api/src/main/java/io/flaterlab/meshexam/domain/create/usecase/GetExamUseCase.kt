package io.flaterlab.meshexam.domain.create.usecase

import io.flaterlab.meshexam.domain.create.model.ExamWithQuestionIdsModel
import io.flaterlab.meshexam.domain.repository.ExamRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetExamUseCase @Inject constructor(
    private val examRepository: ExamRepository,
) {

    operator fun invoke(examId: String): Flow<ExamWithQuestionIdsModel> {
        return examRepository.examWithQuestionIdsByExamId(examId)
    }
}