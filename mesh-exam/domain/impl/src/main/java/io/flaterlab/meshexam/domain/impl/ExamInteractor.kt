package io.flaterlab.meshexam.domain.impl

import io.flaterlab.meshexam.domain.api.datasource.RemoteDataSource
import io.flaterlab.meshexam.domain.api.model.ExamInfoModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import javax.inject.Inject


internal class ExamInteractor @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
) {

    fun discoverExams(): Flow<ExamInfoModel> {
        return emptyFlow()
    }

    fun joinExam(examId: Long) {

    }
}