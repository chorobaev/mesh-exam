package io.flaterlab.meshexam.domain.datasource

import io.flaterlab.meshexam.domain.results.SenderItemModel
import kotlinx.coroutines.flow.Flow

interface ResultReceiverDataSource {

    fun startAcceptingResults(hostingId: String): Flow<List<SenderItemModel>>
}