package io.flaterlab.meshexam.domain.repository

import io.flaterlab.meshexam.domain.profile.model.*
import kotlinx.coroutines.flow.Flow

interface HistoryRepository {

    fun userExamHistory(): Flow<List<ExamHistoryModel>>

    fun hostingResultMeta(hostingId: String): Flow<HostingResultMetaModel>

    fun hostingResults(hostingId: String): Flow<List<HostingResultItemModel>>

    fun individualResultMeta(attemptId: String): Flow<IndividualResultModel>

    fun questionResult(questionId: String, attemptId: String): Flow<QuestionResultModel>
}