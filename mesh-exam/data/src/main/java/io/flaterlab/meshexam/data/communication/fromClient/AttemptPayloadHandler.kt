package io.flaterlab.meshexam.data.communication.fromClient

import androidx.room.withTransaction
import com.google.gson.Gson
import io.flaterlab.meshexam.data.communication.PayloadHandler
import io.flaterlab.meshexam.data.database.MeshDatabase
import io.flaterlab.meshexam.data.database.entity.AttemptAnswerEntity
import io.flaterlab.meshexam.data.database.entity.AttemptEntity
import io.flaterlab.meshexam.data.database.entity.host.AttemptToHostingMapperEntity
import io.flaterlab.meshexam.librariy.mesh.common.dto.FromClientPayload
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import timber.log.Timber
import javax.inject.Inject

internal class AttemptPayloadHandler @Inject constructor(
    private val database: MeshDatabase,
    private val gson: Gson,
) : PayloadHandler.Handler<FromClientPayload> {

    private val attemptDao = database.attemptDao()
    private val attemptAnswerDto = database.attemptAnswerDao()

    override suspend fun handle(payload: FromClientPayload): Boolean {
        if (payload.contentType != AttemptDto.contentType) return false
        val attemptDto = gson.fromJson(payload.data, AttemptDto::class.java)
        saveAttempt(attemptDto)
        return true
    }

    private suspend fun saveAttempt(attempt: AttemptDto) {
        Timber.d("Attempt to save: $attempt")
        database.withTransaction {
            AttemptEntity(
                attemptId = attempt.id,
                userId = attempt.userId,
                examId = attempt.examId,
                status = AttemptEntity.Status.FINISHED,
                score = calculateScore(),
                createdAt = attempt.startedAt,
                updatedAt = attempt.startedAt,
                submittedAt = attempt.finishedAt,
            ).also { attemptDao.insert(it) }

            AttemptToHostingMapperEntity(
                attemptId = attempt.id,
                hostingId = attempt.hostingId,
            ).also { attemptDao.insertAttemptToHostingMapper(it) }

            saveAttemptAnswers(attempt.id, attempt.answers)
        }
    }

    private suspend fun calculateScore(): Int? {
        // TODO: implement score calculation
        return 0
    }

    private suspend fun saveAttemptAnswers(attemptId: String, answers: List<AttemptAnswerDto>) {
        coroutineScope {
            answers.map { answer ->
                async {
                    AttemptAnswerEntity(
                        attemptAnswerId = answer.id,
                        attemptId = attemptId,
                        questionId = answer.questionId,
                        answerId = answer.answerId,
                        createdAt = answer.createdAt,
                    ).also { attemptAnswerDto.insert(it) }
                }
            }.awaitAll()
        }
    }
}