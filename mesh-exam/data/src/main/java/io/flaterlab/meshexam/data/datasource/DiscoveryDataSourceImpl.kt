package io.flaterlab.meshexam.data.datasource

import io.flaterlab.meshexam.domain.api.datasource.DiscoveryDataSource
import io.flaterlab.meshexam.domain.api.model.ExamInfoModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import javax.inject.Inject

internal class DiscoveryDataSourceImpl @Inject constructor(

) : DiscoveryDataSource {

    override fun discoverExams(): Flow<List<ExamInfoModel>> {
        return emptyFlow()
    }

    override suspend fun joinExam(examId: Long) {

    }
}