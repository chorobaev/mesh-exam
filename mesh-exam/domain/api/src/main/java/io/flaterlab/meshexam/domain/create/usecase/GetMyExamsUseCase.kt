package io.flaterlab.meshexam.domain.create.usecase

import io.flaterlab.meshexam.domain.repository.ExamRepository
import io.flaterlab.meshexam.domain.create.model.ExamModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetMyExamsUseCase @Inject constructor(
    private val examRepository: ExamRepository,
) {

    operator fun invoke(): Flow<List<ExamModel>> {
        return examRepository.exams()
    }
}