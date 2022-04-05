package io.flaterlab.meshexam.data.repository

import androidx.room.withTransaction
import io.flaterlab.meshexam.core.Mapper
import io.flaterlab.meshexam.data.database.MeshDatabase
import io.flaterlab.meshexam.data.database.entity.AnswerEntity
import io.flaterlab.meshexam.data.database.entity.ExamEntity
import io.flaterlab.meshexam.data.database.entity.QuestionEntity
import io.flaterlab.meshexam.data.database.entity.update.AnswerContent
import io.flaterlab.meshexam.data.database.entity.update.AnswerCorrectness
import io.flaterlab.meshexam.data.database.entity.update.QuestionContent
import io.flaterlab.meshexam.data.datastore.dao.UserProfileDao
import io.flaterlab.meshexam.data.strategy.IdGeneratorStrategy
import io.flaterlab.meshexam.domain.create.model.*
import io.flaterlab.meshexam.domain.repository.ExamRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import java.util.*
import javax.inject.Inject

internal class ExamRepositoryImpl @Inject constructor(
    private val database: MeshDatabase,
    private val idGenerator: IdGeneratorStrategy,
    private val userProfileDao: UserProfileDao,
    private val examEntityMapper: Mapper<ExamEntity, ExamModel>,
    private val createQuestionModelMapper: Mapper<CreateQuestionModel, QuestionEntity>,
) : ExamRepository {

    private val examDao = database.examDao()
    private val questionDao = database.questionDao()
    private val answerDao = database.answerDao()
    private val hostingDao = database.hostingDao()

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun exams(): Flow<List<ExamModel>> {
        return userProfileDao.userProfile()
            .flatMapLatest { profile ->
                examDao.getExams(profile.id)
                    .map { it.map(examEntityMapper::invoke) }
            }
    }

    override suspend fun createExam(model: CreateExamModel): String {
        val profile = userProfileDao.userProfile().first()
        val entity = ExamEntity(
            examId = UUID.randomUUID().toString(),
            hostUserId = profile.id,
            name = model.name,
            type = model.type ?: "",
            durationInMin = model.durationInMin,
        )
        examDao.insertExams(entity)
        return entity.examId
    }

    override suspend fun getExamWithQuestionIdsByExamId(examId: String): ExamWithQuestionIdsModel {
        return database.withTransaction {
            val exam = examDao.getExamById(examId)
            val questionIds = questionDao.getQuestionIdsByExamId(examId)
            ExamWithQuestionIdsModel(
                exam = examEntityMapper(exam),
                questionIds = questionIds
            )
        }
    }

    override suspend fun getExamByHostingId(hostingId: String): ExamModel {
        return database.withTransaction {
            val hosting = hostingDao.getHostingById(hostingId)
            val exam = examDao.getExamById(hosting.examId)
            ExamModel(
                id = exam.examId,
                name = exam.name,
                type = exam.type,
                durationInMin = exam.durationInMin,
            )
        }
    }


    override fun questionById(questionId: String): Flow<QuestionModel> {
        return questionDao.questionById(questionId)
            .map { entity ->
                QuestionModel(
                    id = entity.questionId,
                    content = entity.question,
                    orderNumber = entity.orderNumber,
                    type = QuestionType.values()
                        .find { it.toString() == entity.type }
                        ?: QuestionType.SINGLE_ANSWER,
                    score = entity.score,
                )
            }
    }

    override suspend fun createQuestion(model: CreateQuestionModel): String {
        val question = createQuestionModelMapper(model)
        questionDao.insertQuestions(question)
        return question.questionId
    }

    override suspend fun deleteQuestions(vararg questionIds: String) {
        questionDao.deleteQuestions(*questionIds)
    }

    override suspend fun updateQuestionContent(questionId: String, content: String) {
        questionDao.updateQuestionContents(
            QuestionContent(questionId, content, Date().time)
        )
    }


    override fun answersByQuestionId(questionId: String): Flow<List<AnswerModel>> {
        return answerDao.answersByQuestionId(questionId)
            .map { list ->
                list.map { entity ->
                    AnswerModel(entity.answerId, entity.answer, entity.isCorrect)
                }
            }
    }

    override suspend fun getAnswerById(answerId: String): AnswerModel {
        return answerDao.getAnswerById(answerId).let { entity ->
            AnswerModel(entity.answerId, entity.answer, entity.isCorrect)
        }
    }

    override suspend fun createAnswer(model: CreateAnswerModel): String {
        val entity = AnswerEntity(
            answerId = idGenerator.generate(),
            hostQuestionId = model.questionId,
            answer = "",
            orderNumber = model.orderNumber,
            isCorrect = false,
            createdAt = Date().time,
            updatedAt = Date().time,
        )
        answerDao.insertAnswers(entity)
        return entity.answerId
    }

    override suspend fun deleteAnswers(vararg answerIds: String) {
        answerDao.deleteAnswers(*answerIds)
    }

    override suspend fun updateAnswerContent(answerId: String, content: String) {
        answerDao.updateAnswerContent(
            AnswerContent(answerId, content, Date().time)
        )
    }

    override suspend fun updateAnswerCorrectness(answerId: String, isCorrect: Boolean) {
        database.withTransaction {
            val answer = answerDao.getAnswerById(answerId)
            val otherAnswers = answerDao.answersByQuestionId(answer.hostQuestionId)
                .first()
                .filter { it.answerId != answerId }
            otherAnswers.asSequence()
                .filter { isCorrect }
                .map { AnswerCorrectness(it.answerId, false, Date().time) }
                .toList()
                .also { answerDao.updateAnswerCorrectness(*it.toTypedArray()) }

            answerDao.updateAnswerCorrectness(
                AnswerCorrectness(answerId, (isCorrect || answer.isCorrect), Date().time)
            )
        }
    }
}