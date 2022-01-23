package io.flaterlab.meshexam.domain.api.datasource

import io.flaterlab.meshexam.domain.api.model.ExamInfoModel
import kotlinx.coroutines.flow.Flow

interface RemoteDataSource {

    fun discoverExams(): Flow<ExamInfoModel>

    suspend fun joinExam(examId: Long)
}