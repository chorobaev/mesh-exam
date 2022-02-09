package io.flaterlab.meshexam.data.repository

import io.flaterlab.meshexam.data.database.MeshDatabase
import io.flaterlab.meshexam.data.datastore.dao.UserProfileDao
import io.flaterlab.meshexam.domain.mesh.model.ClientModel
import io.flaterlab.meshexam.domain.mesh.model.MeshModel
import io.flaterlab.meshexam.domain.repository.MeshRepository
import io.flaterlab.meshexam.librariy.mesh.client.ClientMeshManager
import io.flaterlab.meshexam.librariy.mesh.common.dto.AdvertiserInfo
import io.flaterlab.meshexam.librariy.mesh.common.dto.ClientInfo
import io.flaterlab.meshexam.librariy.mesh.host.HostMeshManager
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import javax.inject.Inject

internal class MeshRepositoryImpl @Inject constructor(
    private val database: MeshDatabase,
    private val profileDao: UserProfileDao,
    private val hostMeshManager: HostMeshManager,
    private val clientMeshManager: ClientMeshManager,
) : MeshRepository {

    private val examDao = database.examDao()

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun startMesh(examId: String): Flow<MeshModel> {
        return flowOf(examId)
            .map { id ->
                val exam = examDao.getExamById(id)
                val profile = profileDao.userProfile().first()
                AdvertiserInfo(
                    hostName = profile.fullName,
                    examId = id,
                    examName = exam.name,
                    examDuration = exam.durationInMin
                )
            }
            .flatMapLatest(hostMeshManager::create)
            .map { result ->
                MeshModel(
                    clients = result.clientList.map { client ->
                        ClientModel(client.id, client.name, client.info, client.status)
                    }
                )
            }
    }

    override suspend fun removeClient(clientId: String) {
        TODO("implement removal when reconnect logic is ready")
    }

    override suspend fun joinExam(examId: String) {
        val profile = profileDao.userProfile().first()
        val clientInfo = ClientInfo(
            id = profile.id,
            name = profile.fullName,
            info = profile.info.orEmpty(),
            status = ""
        )
        clientMeshManager.joinExam(examId, clientInfo)
    }
}