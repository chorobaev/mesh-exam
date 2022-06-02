package io.flaterlab.meshexam.domain.interactor

import io.flaterlab.meshexam.domain.results.SenderItemModel
import kotlinx.coroutines.flow.Flow

interface ResultsInteractor {

    fun startAttemptResultAccepting(hostingId: String): Flow<List<SenderItemModel>>

    suspend fun sendAttemptResult(attemptId: String)
}