package io.flaterlab.meshexam.domain.repository

import io.flaterlab.meshexam.domain.profile.model.ExamHistoryModel
import io.flaterlab.meshexam.domain.profile.model.HostingResultItemModel
import io.flaterlab.meshexam.domain.profile.model.HostingResultMetaModel
import kotlinx.coroutines.flow.Flow

interface HistoryRepository {

    fun userExamHistory(): Flow<List<ExamHistoryModel>>

    fun hostingResultMeta(hostingId: String): Flow<HostingResultMetaModel>

    fun hostingResults(hostingId: String): Flow<List<HostingResultItemModel>>
}