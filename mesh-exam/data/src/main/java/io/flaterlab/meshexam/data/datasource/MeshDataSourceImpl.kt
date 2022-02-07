package io.flaterlab.meshexam.data.datasource

import io.flaterlab.meshexam.data.database.MeshDatabase
import io.flaterlab.meshexam.domain.datasource.MeshDataSource
import io.flaterlab.meshexam.domain.mesh.model.ClientModel
import io.flaterlab.meshexam.domain.mesh.model.MeshModel
import io.flaterlab.meshexam.librariy.mesh.common.dto.AdvertiserInfo
import io.flaterlab.meshexam.librariy.mesh.host.HostMeshManager
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import javax.inject.Inject

internal class MeshDataSourceImpl @Inject constructor(
    private val database: MeshDatabase,
    private val hostMeshManager: HostMeshManager,
) : MeshDataSource {

    private val examDao = database.examDao()

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun startMesh(examId: String): Flow<MeshModel> {
        return flowOf(examId)
            .map { id ->
                val exam = examDao.getExamById(id)
                AdvertiserInfo(
                    hostName = "",
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
        // TODO: implement removal
    }
}