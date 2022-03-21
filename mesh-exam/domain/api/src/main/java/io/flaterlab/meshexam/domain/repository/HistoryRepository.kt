package io.flaterlab.meshexam.domain.repository

import io.flaterlab.meshexam.domain.profile.model.ExamHistoryModel
import io.flaterlab.meshexam.domain.profile.model.HostingResultModel
import kotlinx.coroutines.flow.Flow

interface HistoryRepository {

    fun examHistory(): Flow<List<ExamHistoryModel>>

    fun hostingResults(hostingId: String): Flow<List<HostingResultModel>>
}