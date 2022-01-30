package io.flaterlab.meshexam.data.datasource

import androidx.room.withTransaction
import io.flaterlab.meshexam.core.Mapper
import io.flaterlab.meshexam.data.database.MeshDatabase
import io.flaterlab.meshexam.data.database.entity.AnswerEntity
import io.flaterlab.meshexam.data.database.entity.ExamEntity
import io.flaterlab.meshexam.data.database.entity.QuestionEntity
import io.flaterlab.meshexam.data.database.entity.update.AnswerContent
import io.flaterlab.meshexam.data.database.entity.update.AnswerCorrectness
import io.flaterlab.meshexam.data.database.entity.update.QuestionContent
import io.flaterlab.meshexam.data.strategy.IdGeneratorStrategy
import io.flaterlab.meshexam.domain.api.model.CreateExamModel
import io.flaterlab.meshexam.domain.api.model.ExamModel
import io.flaterlab.meshexam.domain.create.model.*
import io.flaterlab.meshexam.domain.datasource.ExamDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.util.*
import javax.inject.Inject

internal class ExamDataSourceImpl @Inject constructor(
    private val database: MeshDatabase,
    private val idGenerator: IdGeneratorStrategy,
    private val createExamModelMapper: Mapper<CreateExamModel, ExamEntity>,
    private val examEntityMapper: Mapper<ExamEntity, ExamModel>,
    private val createQuestionModelMapper: Mapper<CreateQuestionModel, QuestionEntity>,
) : ExamDataSource {

    private val examDao = database.examDao()
    private val questionDao = database.questionsDao()
    private val answerDao = database.answerDao()

    override fun exams(): Flow<List<ExamModel>> {
        return examDao.getExams().map { it.map(examEntityMapper::invoke) }
    }

    override suspend fun createExam(createExam: CreateExamModel): String {
        val entity = createExamModelMapper(createExam)
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