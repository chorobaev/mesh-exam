package io.flaterlab.meshexam.domain.impl

import io.flaterlab.meshexam.domain.api.datasource.AdvertisingDataSource
import io.flaterlab.meshexam.domain.api.usecase.AdvertiseExamUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AdvertiseExamUseCaseImpl @Inject constructor(
    private val advertiseDataSource: AdvertisingDataSource,
) : AdvertiseExamUseCase {

    override fun invoke(examId: String): Flow<Unit> {
        return advertiseDataSource.startExam(examId)
    }
}