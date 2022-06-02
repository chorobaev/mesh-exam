package io.flaterlab.meshexam.domain.impl

import io.flaterlab.meshexam.domain.datasource.ResultReceiverDataSource
import io.flaterlab.meshexam.domain.datasource.ResultSenderDataSource
import io.flaterlab.meshexam.domain.interactor.ResultsInteractor
import io.flaterlab.meshexam.domain.results.SenderItemModel
import io.flaterlab.meshexam.domain.results.SendingRequestModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ResultsInteractorImpl @Inject constructor(
    private val resultReceiverDataSource: ResultReceiverDataSource,
    private val resultSenderDataSource: ResultSenderDataSource,
) : ResultsInteractor {

    override fun startAttemptResultAccepting(hostingId: String): Flow<List<SenderItemModel>> {
        return resultReceiverDataSource.startAcceptingResults(hostingId)
    }

    override suspend fun sendAttemptResult(attemptId: String) {
        resultSenderDataSource.sendAttemptResult(attemptId)
    }
}