package io.flaterlab.meshexam.domain.create.usecase

import io.flaterlab.meshexam.domain.api.datasource.AdvertisingDataSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AdvertiseExamUseCase @Inject constructor(
    private val advertiseDataSource: AdvertisingDataSource,
) {

    operator fun invoke(examId: String): Flow<Unit> {
        return advertiseDataSource.startExam(examId)
    }
}