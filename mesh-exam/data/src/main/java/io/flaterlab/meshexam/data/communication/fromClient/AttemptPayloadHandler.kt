package io.flaterlab.meshexam.data.communication.fromClient

import androidx.room.withTransaction
import com.google.gson.Gson
import io.flaterlab.meshexam.data.communication.PayloadHandler
import io.flaterlab.meshexam.data.database.MeshDatabase
import io.flaterlab.meshexam.data.database.entity.AttemptAnswerEntity
import io.flaterlab.meshexam.data.database.entity.AttemptEntity
import io.flaterlab.meshexam.librariy.mesh.common.dto.FromClientPayload
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.first
import timber.log.Timber
import javax.inject.Inject

internal class AttemptPayloadHandler @Inject constructor(
    private val database: MeshDatabase,
    private val gson: Gson,
) : PayloadHandler.Handler<FromClientPayload> {

    private val attemptDao = database.attemptDao()
    private val attemptAnswerDto = database.attemptAnswerDao()
    private val answerDao = database.answerDao()
    private val questionDao = database.questionDao()

    override suspend fun handle(payload: FromClientPayload): Boolean {
        if (payload.contentType != AttemptDto.contentType) return false
        val attemptDto = gson.fromJson(payload.data, AttemptDto::class.java)
        saveAttempt(attemptDto)
        return true
    }

    private suspend fun saveAttempt(attempt: AttemptDto) {
        Timber.d("Attempt to save: $attempt")
        database.withTransaction {
            val savedAttempt =
                attemptDao.getAttemptByUserAndHostingId(attempt.userId, attempt.hostingId)
            AttemptEntity(
                attemptId = savedAttempt?.attemptId ?: return@withTransaction,
                userId = attempt.userId,
                examId = attempt.examId,
                status = AttemptEntity.Status.FINISHED,
                score = calculateScore(attempt),
                createdAt = attempt.startedAt,
                updatedAt = attempt.startedAt,
                submittedAt = attempt.finishedAt,
            ).also { attemptDao.update(it) }

            saveAttemptAnswers(attempt.id, attempt.answers)
        }
    }

    private suspend fun calculateScore(attempt: AttemptDto): Float {
        return coroutineScope {
            val answerIds = attempt.answers.map(AttemptAnswerDto::answerId)
            val answers = answerDao.getAnswersByAnswerIds(* answerIds.toTypedArray())
            answers
                .filter { it.isCorrect }
                .map { answerEntity ->
                    async { questionDao.questionById(answerEntity.hostQuestionId).first() }
                }
                .awaitAll()
                .sumOf { it.score.toDouble() }
                .toFloat()
        }
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