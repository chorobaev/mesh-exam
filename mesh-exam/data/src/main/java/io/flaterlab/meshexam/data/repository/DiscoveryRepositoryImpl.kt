package io.flaterlab.meshexam.data.repository

import io.flaterlab.meshexam.domain.api.model.ExamInfoModel
import io.flaterlab.meshexam.domain.repository.DiscoveryRepository
import io.flaterlab.meshexam.librariy.mesh.client.ClientMeshManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

internal class DiscoveryRepositoryImpl @Inject constructor(
    private val clientMeshManager: ClientMeshManager,
) : DiscoveryRepository {

    override fun discoverExams(): Flow<List<ExamInfoModel>> {
        return clientMeshManager.discoverExams()
            .map { result ->
                result.advertiserList.map { info ->
                    ExamInfoModel(info.examId, info.examName, info.hostName, info.examDuration)
                }
            }
    }

    override suspend fun joinExam(examId: String) {
        // TODO: add implementation
    }
}