package io.flaterlab.meshexam.domain.repository

import kotlinx.coroutines.flow.Flow

interface AdvertisingRepository {

    fun startExam(examId: String): Flow<Unit>
}