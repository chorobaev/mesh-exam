package io.flaterlab.meshexam.domain.create.usecase

import io.flaterlab.meshexam.domain.repository.DiscoveryRepository
import io.flaterlab.meshexam.domain.api.model.ExamInfoModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DiscoverExamsUseCase @Inject constructor(
    private val discoveryRepository: DiscoveryRepository,
) {

    operator fun invoke(): Flow<List<ExamInfoModel>> {
        return discoveryRepository.discoverExams()
    }
}