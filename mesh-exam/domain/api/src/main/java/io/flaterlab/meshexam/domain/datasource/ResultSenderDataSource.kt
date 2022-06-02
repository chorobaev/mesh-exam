package io.flaterlab.meshexam.domain.datasource

interface ResultSenderDataSource {

    suspend fun sendAttemptResult(attemptId: String)
}