package io.flaterlab.meshexam.data.repository

import io.flaterlab.meshexam.data.datastore.dao.UserProfileDao
import io.flaterlab.meshexam.domain.api.model.ExamInfoModel
import io.flaterlab.meshexam.domain.repository.DiscoveryRepository
import io.flaterlab.meshexam.librariy.mesh.client.ClientMeshManager
import io.flaterlab.meshexam.librariy.mesh.common.dto.ClientInfo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject

internal class DiscoveryRepositoryImpl @Inject constructor(
    private val clientMeshManager: ClientMeshManager,
    private val profileDao: UserProfileDao,
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
        val profile = profileDao.userProfile().first()
        val clientInfo = ClientInfo(
            id = profile.id,
            name = profile.fullName,
            info = profile.info.orEmpty(),
            positionInMesh = 0,
        )
        clientMeshManager.joinExam(examId, clientInfo)
    }
}