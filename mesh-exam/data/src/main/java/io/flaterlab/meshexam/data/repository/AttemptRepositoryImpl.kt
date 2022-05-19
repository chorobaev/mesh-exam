package io.flaterlab.meshexam.data.repository

import androidx.room.withTransaction
import io.flaterlab.meshexam.core.Mapper
import io.flaterlab.meshexam.data.communication.MeshMessage
import io.flaterlab.meshexam.data.communication.fromClient.AttemptAnswerDto
import io.flaterlab.meshexam.data.communication.fromClient.AttemptDto
import io.flaterlab.meshexam.data.communication.fromClient.ExamEventDto
import io.flaterlab.meshexam.data.database.MeshDatabase
import io.flaterlab.meshexam.data.database.entity.AttemptAnswerEntity
import io.flaterlab.meshexam.data.database.entity.AttemptEntity
import io.flaterlab.meshexam.data.database.entity.update.AttemptFinishing
import io.flaterlab.meshexam.data.datastore.dao.UserProfileDao
import io.flaterlab.meshexam.data.strategy.IdGeneratorStrategy
import io.flaterlab.meshexam.data.worker.WorkerScheduler
import io.flaterlab.meshexam.domain.exam.model.*
import io.flaterlab.meshexam.domain.repository.AttemptRepository
import io.flaterlab.meshexam.librariy.mesh.client.ClientMeshManager
import io.flaterlab.meshexam.librariy.mesh.common.dto.FromClientPayload
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.withContext
import java.util.*
import javax.inject.Inject
import kotlin.math.max

internal class AttemptRepositoryImpl @Inject constructor(
    private val userProfileDao: UserProfileDao,
    private val database: MeshDatabase,
    private val idGenerator: IdGeneratorStrategy,
    private val workerScheduler: WorkerScheduler,
    private val clientMeshManager: ClientMeshManager,
    private val clientMessageMapper: Mapper<MeshMessage, FromClientPayload>,
) : AttemptRepository {

    private val examDao = database.examDao()
    private val attemptDao = database.attemptDao()
    private val attemptAnswerDao = database.attemptAnswerDao()

    override suspend fun attempt(examId: String): String {
        val profile = userProfileDao.userProfile().first()
        return database.withTransaction {
            val exam = examDao.getExamById(examId)
            val hostingId = examDao.getHostingIdByExamId(examId)
            val attempt = AttemptEntity(
                attemptId = idGenerator.generate(),
                userId = profile.id,
                examId = exam.examId,
                hostingId = hostingId,
                status = AttemptEntity.Status.STARTED,
                score = null,
                createdAt = Date().time,
                updatedAt = Date().time,
                submittedAt = null,
            )
            attemptDao.insert(attempt)
            workerScheduler.scheduleAttemptFinish(attempt.attemptId, exam.durationInMin)
            attempt.attemptId
        }
    }

    override fun attemptMetaById(attemptId: String): Flow<AttemptMetaModel> {
        return attemptDao.attemptById(attemptId)
            .map { attempt ->
                val exam = examDao.getExamById(attempt.examId)
                AttemptMetaModel(
                    examId = exam.examId,
                    attemptId = attemptId,
                    examName = exam.name,
                    examInfo = exam.type,
                    examStatus = resolveAttemptMetaStatus(attempt),
                    leftTimeInMillis = exam.durationInMin * 60 * 1000 - (Date().time - attempt.createdAt)
                )
            }
    }

    private fun resolveAttemptMetaStatus(attempt: AttemptEntity) =
        when (attempt.status) {
            AttemptEntity.Status.STARTED -> AttemptMetaModel.ExamStatus.STARTED
            AttemptEntity.Status.FINISHED -> AttemptMetaModel.ExamStatus.FINISHED
        }

    override suspend fun getActiveAttempts(): List<AttemptMetaModel> {
        return attemptDao
            .getActiveAttempts()
            .map { attempt ->
                val exam = examDao.getExamById(attempt.examId)
                AttemptMetaModel(
                    examId = exam.examId,
                    attemptId = attempt.attemptId,
                    examName = exam.name,
                    examInfo = exam.type,
                    examStatus = resolveAttemptMetaStatus(attempt),
                    leftTimeInMillis = exam.durationInMin * 60 * 1000 - (Date().time - attempt.createdAt)
                )
            }
    }

    override suspend fun addAttemptAnswer(model: SelectAnswerModel) {
        val prevAnswer = attemptAnswerDao
            .attemptAnswerByAttemptAndQuestionId(model.questionId, model.attemptId)
            .firstOrNull()
        if (prevAnswer == null || prevAnswer.answerId != model.answerId) {
            val newAnswer = AttemptAnswerEntity(
                attemptAnswerId = idGenerator.generate(),
                attemptId = model.attemptId,
                questionId = model.questionId,
                answerId = model.answerId,
                createdAt = Date().time
            )
            attemptAnswerDao.insert(newAnswer)
        }
    }

    override suspend fun finishAttemptBySystem(attemptId: String) {
        val attemptFinishing = AttemptFinishing(
            attemptId = attemptId,
            score = 0,
            status = AttemptEntity.Status.FINISHED,
            submittedAt = Date().time
        )
        attemptDao.updateToFinishAttempt(attemptFinishing)
        sendAttempt(attemptId)
    }

    override suspend fun finishAttempt(attemptId: String) {
        workerScheduler.cancelAllAttemptFinishers()
        finishAttemptBySystem(attemptId)
    }

    private suspend fun sendAttempt(attemptId: String) = withContext(Dispatchers.IO) {
        val attemptDto = database.withTransaction {
            val attemptEntity = attemptDao.getAttemptById(attemptId)
            val answersEntity = attemptAnswerDao.getAnswersByAttemptId(attemptId)
            val hostingId = examDao.getHostingIdByExamId(attemptEntity.examId)
            AttemptDto(
                id = attemptEntity.attemptId,
                userId = attemptEntity.userId,
                examId = attemptEntity.examId,
                hostingId = hostingId,
                startedAt = attemptEntity.createdAt,
                finishedAt = attemptEntity.submittedAt
                    ?: throw IllegalArgumentException("Exam must be submitted before sending"),
                answers = answersEntity.map { entity ->
                    AttemptAnswerDto(
                        id = entity.attemptAnswerId,
                        questionId = entity.questionId,
                        answerId = entity.answerId,
                        createdAt = entity.createdAt,
                    )
                }
            )
        }
        clientMeshManager.sendPayloadToHost(clientMessageMapper(attemptDto))
    }

    override fun selectedAnswerByAttemptAndQuestionId(
        attemptId: String,
        questionId: String
    ): Flow<SelectedAnswerModel> {
        return attemptAnswerDao
            .attemptAnswerByAttemptAndQuestionId(attemptId, questionId)
            .map { entity ->
                SelectedAnswerModel(
                    answerId = entity?.answerId
                )
            }
    }

    override fun attemptTimeLeftInSec(attemptId: String): Flow<Int> {
        return flow {
            val attempt = attemptDao.getAttemptById(attemptId)
            val exam = examDao.getExamById(attempt.examId)
            val millisPassed = Date().time - attempt.createdAt
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

    override fun attemptResult(attemptId: String): Flow<AttemptResultModel> {
        return flow {
            val result = database.withTransaction {
                val attempt = attemptDao.getAttemptById(attemptId)
                val exam = examDao.getExamById(attempt.examId)
                AttemptResultModel(
                    examId = attempt.examId,
                    attemptId = attemptId,
                    examName = exam.name,
                    durationInMillis = max(attempt.submittedAt?.minus(attempt.createdAt) ?: 0, 0)
                )
            }
            emit(result)
        }
    }

    override suspend fun sendExamEvent(attemptId: String, event: ExamEvent) {
        val userProfile = userProfileDao.userProfile().first()
        val eventType = when (event) {
            ExamEvent.SCREEN_HID -> ExamEventDto.EventType.SCREEN_HID
            ExamEvent.SCREEN_VISIBLE -> ExamEventDto.EventType.SCREEN_VISIBLE
            else -> throw IllegalArgumentException("No such event with int: ${event.typeInt}")
        }
        val examEventDto = ExamEventDto(
            clientId = userProfile.id,
            hostingId = attemptDao.getAttemptById(attemptId).hostingId,
            eventType = eventType
        )
        clientMeshManager.sendPayloadToHost(clientMessageMapper(examEventDto))
    }
}