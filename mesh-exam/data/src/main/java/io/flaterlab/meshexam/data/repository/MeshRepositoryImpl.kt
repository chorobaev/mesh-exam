package io.flaterlab.meshexam.data.repository

import androidx.room.withTransaction
import io.flaterlab.meshexam.core.Mapper
import io.flaterlab.meshexam.data.communication.MeshMessage
import io.flaterlab.meshexam.data.communication.PayloadHandler
import io.flaterlab.meshexam.data.communication.fromClient.ExamEventDto
import io.flaterlab.meshexam.data.communication.fromHost.AnswerDto
import io.flaterlab.meshexam.data.communication.fromHost.ExamDto
import io.flaterlab.meshexam.data.communication.fromHost.FinishExamEventDto
import io.flaterlab.meshexam.data.communication.fromHost.QuestionDto
import io.flaterlab.meshexam.data.database.MeshDatabase
import io.flaterlab.meshexam.data.database.entity.AttemptEntity
import io.flaterlab.meshexam.data.database.entity.UserEntity
import io.flaterlab.meshexam.data.database.entity.host.HostingEntity
import io.flaterlab.meshexam.data.datastore.dao.UserProfileDao
import io.flaterlab.meshexam.data.strategy.IdGeneratorStrategy
import io.flaterlab.meshexam.data.worker.WorkerScheduler
import io.flaterlab.meshexam.domain.exam.model.ExamEvent
import io.flaterlab.meshexam.domain.exam.model.ExamEventModel
import io.flaterlab.meshexam.domain.mesh.model.*
import io.flaterlab.meshexam.domain.repository.MeshRepository
import io.flaterlab.meshexam.librariy.mesh.common.dto.AdvertiserInfo
import io.flaterlab.meshexam.librariy.mesh.common.dto.ClientInfo
import io.flaterlab.meshexam.librariy.mesh.common.dto.FromClientPayload
import io.flaterlab.meshexam.librariy.mesh.common.dto.FromHostPayload
import io.flaterlab.meshexam.librariy.mesh.host.HostMeshManager
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import timber.log.Timber
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.math.max

@Singleton
internal class MeshRepositoryImpl @Inject constructor(
    private val database: MeshDatabase,
    private val profileDao: UserProfileDao,
    private val hostMeshManager: HostMeshManager,
    private val idGenerator: IdGeneratorStrategy,
    private val workerScheduler: WorkerScheduler,
    private val userProfileDao: UserProfileDao,
    private val hostMessageMapper: Mapper<MeshMessage, FromHostPayload>,
    private val fromClientPayloadHandler: PayloadHandler<FromClientPayload>,
) : MeshRepository {

    private val examDao = database.examDao()
    private val questionDao = database.questionDao()
    private val answerDao = database.answerDao()
    private val attemptDao = database.attemptDao()
    private val hostingDao = database.hostingDao()
    private val userDao = database.userDao()
    private val examEventDao = database.examEventDao()

    private val coroutineScope = CoroutineScope(Dispatchers.Default + SupervisorJob())

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

    override suspend fun destroyMesh(examId: String) = hostMeshManager.stop()

    override suspend fun removeClient(clientId: String) {
        // TODO: implement client removal
    }

    override suspend fun startExam(examId: String): StartExamResultModel {
        return withContext(Dispatchers.IO) {
            val user = userProfileDao.userProfile().first()
            val exam = examDao.getExamById(examId)
            val hosting = HostingEntity(
                hostingId = idGenerator.generate(),
                userId = user.id,
                examId = examId,
                startedAt = Date().time,
                finishedAt = null,
            )
            hostingDao.insert(hosting)
            saveUsers(examId, hosting.hostingId)
            sendExamContent(examId, hosting.hostingId)
            workerScheduler.scheduleHostingFinish(hosting.hostingId, exam.durationInMin)
            StartExamResultModel(examId, hosting.hostingId)
        }
    }

    private suspend fun saveUsers(examId: String, hostingId: String) {
        database.withTransaction {
            val userEntities = hostMeshManager
                .connectedClientsFlow.value
                .map { client ->
                    UserEntity(
                        userId = client.id,
                        fullName = client.name,
                        info = client.info,
                    )
                }
            userDao.insert(*userEntities.toTypedArray())
            val emptyAttempts = userEntities
                .map { user ->
                    AttemptEntity(
                        attemptId = idGenerator.generate(),
                        userId = user.userId,
                        examId = examId,
                        hostingId = hostingId,
                        status = AttemptEntity.Status.STARTED,
                        score = null,
                        createdAt = Date().time,
                        updatedAt = Date().time,
                        submittedAt = null,
                    )
                }
            attemptDao.insert(*emptyAttempts.toTypedArray())
        }
    }

    private suspend fun sendExamContent(examId: String, hostingId: String) = coroutineScope {
        val examEntity = examDao.getExamById(examId)
        val questionEntities = questionDao.getQuestionsByExamId(examId)
        val questions = questionEntities.map { questionEntity ->
            async {
                questionEntity to
                        answerDao.answersByQuestionId(questionEntity.questionId).first()
            }
        }
            .awaitAll()
            .map { (questionEntity, answers) ->
                QuestionDto(
                    id = questionEntity.questionId,
                    answers = answers.map { answerEntity ->
                        AnswerDto(
                            id = answerEntity.answerId,
                            answer = answerEntity.answer,
                            isCorrect = answerEntity.isCorrect,
                            orderNumber = answerEntity.orderNumber,
                        )
                    },
                    orderNumber = questionEntity.orderNumber,
                    question = questionEntity.question,
                    score = questionEntity.score,
                    type = questionEntity.type,
                )
            }
        val exam = ExamDto(
            id = examEntity.examId,
            hostUserId = examEntity.hostUserId,
            hostingId = hostingId,
            durationInMin = examEntity.durationInMin,
            name = examEntity.name,
            questions = questions,
            type = examEntity.type,
        )
        hostMeshManager.sendPayload(hostMessageMapper(exam))
        registerAttemptReceiver()
    }

    private fun registerAttemptReceiver() {
        hostMeshManager.onPayloadFromClientListener = { payload ->
            coroutineScope.launch {
                fromClientPayloadHandler.handle(payload)
            }
        }
    }

    override suspend fun finishExamBySystem(hostingId: String) {
        database.withTransaction {
            val hosting = hostingDao.getHostingById(hostingId)
            hostingDao.update(hosting.copy(finishedAt = Date().time))
        }
    }

    override suspend fun finishExam(hostingId: String) {
        workerScheduler.cancelAllHostingFinishers()
        sendFinishNotification(hostingId)
        finishExamBySystem(hostingId)
    }

    private suspend fun sendFinishNotification(hostingId: String) {
        val finishExamAction = FinishExamEventDto(hostingId)
        hostMeshManager.sendPayload(hostMessageMapper(finishExamAction))
    }

    override fun hostingMetaModel(hostingId: String): Flow<HostingMetaModel> {
        return hostingDao.hostingById(hostingId)
            .map { entity ->
                HostingMetaModel(
                    status = if (entity.finishedAt == null) {
                        HostingMetaModel.HostingStatus.STARTED
                    } else {
                        HostingMetaModel.HostingStatus.FINISHED
                    }
                )
            }
    }

    override fun hostingTimeLeftInSec(hostingId: String): Flow<Int> {
        return flow {
            val hosting = hostingDao.getHostingById(hostingId)
            val exam = examDao.getExamById(hosting.examId)
            val millisPassed = Date().time - hosting.startedAt
            val secondsPassed = (millisPassed / 1000).toInt()
            delay(millisPassed % 1000)

            var secondsLeft: Int = exam.durationInMin * 60 - secondsPassed

            do {
                emit(max(secondsLeft, 0))
                delay(1000)
                secondsLeft--
            } while (secondsLeft >= 0)
        }
    }

    override fun hostedStudentList(
        hostingId: String,
        searchText: String?,
    ): Flow<List<HostedStudentModel>> {
        return combine(
            userDao.searchUsersByHostingId(hostingId, searchText.orEmpty()),
            hostMeshManager.connectedClientsFlow,
        ) { userList: List<UserEntity>, meshClientList: List<ClientInfo> ->
            Timber.d("Users for hosting id: $hostingId, $userList")
            userList.map { user ->
                HostedStudentModel(
                    userId = user.userId,
                    fullName = user.fullName,
                    info = user.info,
                    status = provideHostedStudentStatus(user, hostingId, meshClientList)
                )
            }
        }
    }

    private suspend fun provideHostedStudentStatus(
        user: UserEntity,
        hostingId: String,
        connectedUsers: List<ClientInfo>,
    ): HostedStudentModel.Status {
        val attempt = attemptDao.getAttemptByUserAndHostingId(user.userId, hostingId)
        return when {
            attempt?.isFinished == true ->
                HostedStudentModel.Status.SUBMITTED
            connectedUsers.all { it.id != user.userId } ->
                HostedStudentModel.Status.DISCONNECTED
            else ->
                HostedStudentModel.Status.ATTEMPTING
        }
    }

    override fun examEvents(hostingId: String): Flow<List<ExamEventModel>> {
        return examEventDao.examEvents(hostingId)
            .map { list ->
                list.map { event ->
                    ExamEventModel(
                        userId = event.authorClientId,
                        userFullName = userDao.getUserById(event.authorClientId).fullName,
                        event = resolveEventType(ExamEventDto.EventType.fromInt(event.eventType)),
                        time = Date(event.createdAt),
                    )
                }
            }
    }

    private fun resolveEventType(type: ExamEventDto.EventType): ExamEvent {
        return when (type) {
            ExamEventDto.EventType.SCREEN_HID -> ExamEvent.SCREEN_HID
            ExamEventDto.EventType.SCREEN_VISIBLE -> ExamEvent.SCREEN_VISIBLE
            else -> throw IllegalArgumentException("No such event type: $type")
        }
    }
}