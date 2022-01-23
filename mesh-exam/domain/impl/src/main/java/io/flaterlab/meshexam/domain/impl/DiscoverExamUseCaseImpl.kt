package io.flaterlab.meshexam.domain.impl

import io.flaterlab.meshexam.domain.api.datasource.RemoteDataSource
import io.flaterlab.meshexam.domain.api.model.ExamInfoModel
import io.flaterlab.meshexam.domain.api.usecase.DiscoverExamsUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DiscoverExamUseCaseImpl @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
): DiscoverExamsUseCase {

    override fun invoke(): Flow<ExamInfoModel> {
        return remoteDataSource.discoverExams()
    }
}