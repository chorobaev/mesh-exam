package io.flaterlab.meshexam.domain.api.usecase

import io.flaterlab.meshexam.domain.api.model.ExamModel
import kotlinx.coroutines.flow.Flow

interface GetMyExamUseCase {

    operator fun invoke(): Flow<List<ExamModel>>
}