package io.flaterlab.meshexam.data.datasource

import io.flaterlab.meshexam.domain.api.datasource.RemoteDataSource
import io.flaterlab.meshexam.domain.api.model.ExamInfoModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

internal class RemoteDataSourceImpl @Inject constructor(

) : RemoteDataSource {

    override fun discoverExams(): Flow<ExamInfoModel> {
        return flowOf(
            *(0..5L).map {
                ExamInfoModel(
                    id = it,
                    name = "Exam #$it",
                    host = "Nurbol",
                    duration = 1000L,
                )
            }.toTypedArray()
        )
    }

    override suspend fun joinExam(examId: Long) {

    }
}