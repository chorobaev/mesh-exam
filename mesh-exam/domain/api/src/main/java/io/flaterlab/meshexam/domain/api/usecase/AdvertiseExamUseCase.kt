package io.flaterlab.meshexam.domain.api.usecase

import kotlinx.coroutines.flow.Flow

interface AdvertiseExamUseCase {

    operator fun invoke(examId: String): Flow<Unit>
}