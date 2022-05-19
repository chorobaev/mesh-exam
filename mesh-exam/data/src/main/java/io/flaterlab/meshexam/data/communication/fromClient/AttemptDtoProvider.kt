package io.flaterlab.meshexam.data.communication.fromClient

import androidx.room.withTransaction
import io.flaterlab.meshexam.data.database.MeshDatabase
import javax.inject.Inject

internal class AttemptDtoProvider @Inject constructor(
    private val database: MeshDatabase,
) {

    suspend fun provide(attemptId: String): AttemptDto {
        return database.withTransaction {
            val attemptEntity = database.attemptDao().getAttemptById(attemptId)
            val answersEntity = database.attemptAnswerDao().getAnswersByAttemptId(attemptId)
            val hostingId = database.examDao().getHostingIdByExamId(attemptEntity.examId)
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
    }
}