package io.flaterlab.meshexam.domain.api.usecase

import io.flaterlab.meshexam.domain.api.model.ExamInfoModel
import kotlinx.coroutines.flow.Flow

interface DiscoverExamsUseCase {

    operator fun invoke(): Flow<ExamInfoModel>
}