package io.flaterlab.meshexam.domain.create.usecase

import io.flaterlab.meshexam.domain.repository.AdvertisingRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AdvertiseExamUseCase @Inject constructor(
    private val advertiseRepository: AdvertisingRepository,
) {

    operator fun invoke(examId: String): Flow<Unit> {
        return advertiseRepository.startExam(examId)
    }
}