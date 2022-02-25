package io.flaterlab.meshexam.data.repository

import androidx.room.withTransaction
import io.flaterlab.meshexam.data.database.MeshDatabase
import io.flaterlab.meshexam.data.database.entity.AttemptAnswerEntity
import io.flaterlab.meshexam.data.database.entity.AttemptEntity
import io.flaterlab.meshexam.data.database.entity.update.AttemptFinishing
import io.flaterlab.meshexam.data.datastore.dao.UserProfileDao
import io.flaterlab.meshexam.data.strategy.IdGeneratorStrategy
import io.flaterlab.meshexam.domain.exam.model.AttemptMetaModel
import io.flaterlab.meshexam.domain.exam.model.SelectAnswerModel
import io.flaterlab.meshexam.domain.exam.model.SelectedAnswerModel
import io.flaterlab.meshexam.domain.repository.AttemptRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import java.util.*
import javax.inject.Inject

internal class AttemptRepositoryImpl @Inject constructor(
    private val userProfileDao: UserProfileDao,
    private val database: MeshDatabase,
    private val idGenerator: IdGeneratorStrategy,
) : AttemptRepository {

    private val examDao = database.examDao()
    private val attemptDao = database.attemptDao()
    private val attemptAnswerDao = database.attemptAnswerDao()

    override suspend fun attempt(examId: String): String {
        val profile = userProfileDao.userProfile().first()
        return database.withTransaction {
            val exam = examDao.getExamById(examId)
            val attempt = AttemptEntity(
                attemptId = idGenerator.generate(),
                userId = profile.id,
                examId = exam.examId,
                status = AttemptEntity.Status.STARTED,
                score = null,
                createdAt = Date().time,
                updatedAt = Date().time,
                submittedAt = null,
            )
            attemptDao.insert(attempt)
            attempt.attemptId
        }
    }

    override suspend fun getAttemptMetaById(attemptId: String): AttemptMetaModel {
        val attempt = attemptDao.getAttemptById(attemptId)
        val exam = examDao.getExamById(attempt.examId)
        return AttemptMetaModel(
            examId = exam.examId,
            attemptId = attemptId,
            examName = exam.name,
            examInfo = exam.type,
            leftTimeInMillis = exam.durationInMin * 60 * 1000 - (Date().time - attempt.createdAt)
        )
    }

    override suspend fun addAttemptAnswer(model: SelectAnswerModel) {
        val prevAnswer = attemptAnswerDao
            .attemptAnswerByQuestionId(model.questionId)
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

    override suspend fun finishAttempt(attemptId: String) {
        val attemptFinishing = AttemptFinishing(
            attemptId = attemptId,
            score = 0,
            submittedAt = Date().time
        )
        attemptDao.updateToFinishAttempt(attemptFinishing)
    }

    override fun selectedAnswerByQuestionId(questionId: String): Flow<SelectedAnswerModel> {
        return attemptAnswerDao
            .attemptAnswerByQuestionId(questionId)
            .map { entity ->
                SelectedAnswerModel(
                    answerId = entity?.answerId
                )
            }
    }
}