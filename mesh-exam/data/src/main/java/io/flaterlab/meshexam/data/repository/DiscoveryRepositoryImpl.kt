package io.flaterlab.meshexam.data.repository

import io.flaterlab.meshexam.data.communication.PayloadHandler
import io.flaterlab.meshexam.data.communication.fromHost.ExamDto
import io.flaterlab.meshexam.data.datastore.dao.UserProfileDao
import io.flaterlab.meshexam.domain.create.model.ExamInfoModel
import io.flaterlab.meshexam.domain.exam.model.ExamStateModel
import io.flaterlab.meshexam.domain.repository.AttemptRepository
import io.flaterlab.meshexam.domain.repository.DiscoveryRepository
import io.flaterlab.meshexam.librariy.mesh.client.ClientMeshManager
import io.flaterlab.meshexam.librariy.mesh.common.dto.ClientInfo
import io.flaterlab.meshexam.librariy.mesh.common.dto.FromHostPayload
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class DiscoveryRepositoryImpl @Inject constructor(
    private val clientMeshManager: ClientMeshManager,
    private val profileDao: UserProfileDao,
    private val attemptRepository: AttemptRepository,
    private val fromHostPayloadHandler: PayloadHandler<FromHostPayload>,
) : DiscoveryRepository {

    private val _examState = MutableStateFlow<ExamStateModel>(ExamStateModel.None)

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
        val advertiserInfo = clientMeshManager.joinExam(examId, clientInfo)
        _examState.value = ExamStateModel.Waiting(
            examId = examId,
            examName = advertiserInfo.examName,
        )
        registerPayloadReceived()
    }

    private fun registerPayloadReceived() {
        clientMeshManager.onPayloadFromHostCallback = { fromHostPayload ->
            Timber.d("FromHostPayload inside discovery: $fromHostPayload")
            fromHostPayloadHandler.handle(fromHostPayload)
            when (fromHostPayload.contentType) {
                ExamDto.contentType -> onExamStarted()
            }
        }
    }

    private suspend fun onExamStarted() {
        val prevState = _examState.value as ExamStateModel.Waiting
        _examState.value = ExamStateModel.Started(
            examId = prevState.examId,
            attemptId = attemptRepository.attempt(prevState.examId)
        )
    }

    override fun examState(examId: String): Flow<ExamStateModel> = _examState

    override suspend fun leaveExam(examId: String) {
        clientMeshManager.leaveExam(examId)
    }
}