package io.flaterlab.meshexam.domain.create.usecase

import io.flaterlab.meshexam.domain.datasource.DiscoveryDataSource
import io.flaterlab.meshexam.domain.api.model.ExamInfoModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DiscoverExamsUseCase @Inject constructor(
    private val discoveryDataSource: DiscoveryDataSource,
) {

    operator fun invoke(): Flow<List<ExamInfoModel>> {
        return discoveryDataSource.discoverExams()
    }
}