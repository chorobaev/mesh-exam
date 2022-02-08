package io.flaterlab.meshexam.domain.repository

import io.flaterlab.meshexam.domain.api.model.ExamInfoModel
import kotlinx.coroutines.flow.Flow

interface DiscoveryRepository {

    fun discoverExams(): Flow<List<ExamInfoModel>>

    suspend fun joinExam(examId: String)
}