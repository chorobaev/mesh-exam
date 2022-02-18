package io.flaterlab.meshexam.domain.repository

import io.flaterlab.meshexam.domain.api.model.ExamInfoModel
import io.flaterlab.meshexam.domain.exam.model.ExamStateModel
import kotlinx.coroutines.flow.Flow

interface DiscoveryRepository {

    fun discoverExams(): Flow<List<ExamInfoModel>>

    suspend fun joinExam(examId: String)

    fun examState(examId: String): Flow<ExamStateModel>

    suspend fun leaveExam(examId: String)
}