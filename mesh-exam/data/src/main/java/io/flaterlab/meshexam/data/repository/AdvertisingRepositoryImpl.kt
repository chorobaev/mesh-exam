package io.flaterlab.meshexam.data.repository

import io.flaterlab.meshexam.domain.repository.AdvertisingRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import javax.inject.Inject

internal class AdvertisingRepositoryImpl @Inject constructor(

) : AdvertisingRepository {

    override fun startExam(examId: String): Flow<Unit> {
        // TODO: add actual info
        return emptyFlow()
    }
}