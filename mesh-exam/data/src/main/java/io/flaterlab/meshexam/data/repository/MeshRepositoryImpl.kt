package io.flaterlab.meshexam.data.repository

import io.flaterlab.meshexam.core.Mapper
import io.flaterlab.meshexam.data.communication.AnswerDto
import io.flaterlab.meshexam.data.communication.ExamDto
import io.flaterlab.meshexam.data.communication.HostMessage
import io.flaterlab.meshexam.data.communication.QuestionDto
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
import io.flaterlab.meshexam.librariy.mesh.host.HostMeshManager
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class MeshRepositoryImpl @Inject constructor(
    private val database: MeshDatabase,
    private val profileDao: UserProfileDao,
    private val hostMeshManager: HostMeshManager,
    private val idGenerator: IdGeneratorStrategy,
    private val userProfileDao: UserProfileDao,
    private val hostMessageMapper: Mapper<HostMessage, FromHostPayload>,
) : MeshRepository {

    private val examDao = database.examDao()
    private val questionDao = database.questionDao()
    private val answerDao = database.answerDao()
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
            sendExamContent(examId)
            StartExamResultModel(examId, hosting.hostingId)
        }
    }

    private suspend fun sendExamContent(examId: String) {
        coroutineScope {
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
                durationInMin = examEntity.durationInMin,
                name = examEntity.name,
                questions = questions,
                type = examEntity.type
            )
            hostMeshManager.sendPayload(hostMessageMapper(exam))
        }
    }
}