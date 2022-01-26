package io.flaterlab.meshexam.domain.api.datasource

import kotlinx.coroutines.flow.Flow

interface AdvertisingDataSource {

    fun startExam(examId: String): Flow<Unit>
}