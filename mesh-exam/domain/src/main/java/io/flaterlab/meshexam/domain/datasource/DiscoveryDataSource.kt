package io.flaterlab.meshexam.domain.api.datasource

import io.flaterlab.meshexam.domain.api.model.ExamInfoModel
import kotlinx.coroutines.flow.Flow

interface DiscoveryDataSource {

    fun discoverExams(): Flow<List<ExamInfoModel>>

    suspend fun joinExam(examId: Long)
}