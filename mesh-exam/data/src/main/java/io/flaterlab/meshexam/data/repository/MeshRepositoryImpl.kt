package io.flaterlab.meshexam.data.repository

import io.flaterlab.meshexam.data.database.MeshDatabase
import io.flaterlab.meshexam.data.database.entity.HostingEntity
import io.flaterlab.meshexam.data.datastore.dao.UserProfileDao
import io.flaterlab.meshexam.data.strategy.IdGeneratorStrategy
import io.flaterlab.meshexam.domain.mesh.model.ClientModel
import io.flaterlab.meshexam.domain.mesh.model.MeshModel
import io.flaterlab.meshexam.domain.mesh.model.StartExamResultModel
import io.flaterlab.meshexam.domain.repository.MeshRepository
import io.flaterlab.meshexam.librariy.mesh.common.dto.AdvertiserInfo
import io.flaterlab.meshexam.librariy.mesh.common.dto.FromHostPayload
import io.flaterlab.meshexam.librariy.mesh.common.dto.StartExamPayload
import io.flaterlab.meshexam.librariy.mesh.host.HostMeshManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.withContext
import javax.inject.Inject

internal class MeshRepositoryImpl @Inject constructor(
    private val database: MeshDatabase,
    private val profileDao: UserProfileDao,
    private val hostMeshManager: HostMeshManager,
    private val idGenerator: IdGeneratorStrategy,
    private val userProfileDao: UserProfileDao,
) : MeshRepository {

    private val examDao = database.examDao()
    private val hostingDao = database.hostingDao()

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun createMesh(examId: String): Flow<MeshModel> {
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
                        ClientModel(client.id, client.name, client.info, client.positionInMesh)
                    }
                )
            }
    }

    override fun stopMesh() = hostMeshManager.stop()

    override suspend fun removeClient(clientId: String) {
        TODO("implement removal when reconnect logic is ready")
    }

    override suspend fun startExam(examId: String): StartExamResultModel {
        return withContext(Dispatchers.IO) {
            val user = userProfileDao.userProfile().first()
            val hosting = HostingEntity(
                hostingId = idGenerator.generate(),
                userId = user.id,
                examId = examId,
            )
            hostingDao.insert(hosting)
            hostMeshManager.sendPayload(
                FromHostPayload(
                    type = StartExamPayload.TYPE_KEY,
                    data = ""
                )
            )
            StartExamResultModel(examId, hosting.hostingId)
        }
    }
}