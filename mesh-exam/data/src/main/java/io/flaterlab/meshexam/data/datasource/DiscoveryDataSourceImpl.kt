package io.flaterlab.meshexam.data.datasource

import io.flaterlab.meshexam.domain.api.model.ExamInfoModel
import io.flaterlab.meshexam.domain.datasource.DiscoveryDataSource
import io.flaterlab.meshexam.librariy.mesh.client.ClientMeshManager
import io.flaterlab.meshexam.librariy.mesh.common.dto.MeshResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

internal class DiscoveryDataSourceImpl @Inject constructor(
    private val clientMeshManager: ClientMeshManager,
) : DiscoveryDataSource {

    override fun discoverExams(): Flow<List<ExamInfoModel>> {
        return clientMeshManager.discoverExams()
            .map { result ->
                when (result) {
                    is MeshResult.Success -> result.data.advertiserList.map { info ->
                        ExamInfoModel(info.examId, info.examName, info.hostName, info.examDuration)
                    }
                    is MeshResult.Error -> throw result.cause
                }
            }
    }

    override suspend fun joinExam(examId: Long) {

    }
}