package io.flaterlab.meshexam.data.communication.fromHost

import androidx.room.withTransaction
import com.google.gson.Gson
import io.flaterlab.meshexam.data.communication.PayloadHandler
import io.flaterlab.meshexam.data.database.MeshDatabase
import io.flaterlab.meshexam.data.database.entity.AnswerEntity
import io.flaterlab.meshexam.data.database.entity.ExamEntity
import io.flaterlab.meshexam.data.database.entity.QuestionEntity
import io.flaterlab.meshexam.data.database.entity.client.ExamToHostingMapperEntity
import io.flaterlab.meshexam.librariy.mesh.common.dto.FromHostPayload
import timber.log.Timber
import java.util.*
import javax.inject.Inject

internal class ExamContentPayloadHandler @Inject constructor(
    private val database: MeshDatabase,
    private val gson: Gson,
) : PayloadHandler.Handler<FromHostPayload> {

    private val examDao = database.examDao()
    private val questionDao = database.questionDao()
    private val answerDao = database.answerDao()

    override suspend fun handle(payload: FromHostPayload): Boolean {
        if (payload.contentType != ExamDto.contentType) return false
        val examDto = gson.fromJson(payload.data, ExamDto::class.java)
        saveExam(examDto)
        return true
    }

    private suspend fun saveExam(exam: ExamDto) {
        Timber.d("ExamDto to be saved: $exam")
        database.withTransaction {
            ExamEntity(
                examId = exam.id,
                hostUserId = exam.hostUserId,
                name = exam.name,
                type = exam.type,
                durationInMin = exam.durationInMin
            ).also { examDao.insertExams(it) }

            ExamToHostingMapperEntity(
                examId = exam.id,
                hostingId = exam.hostingId,
            ).also { examDao.insertExamToHostingMapper(it) }

            saveQuestionList(exam.id, exam.questions)
        }
    }

    private suspend fun saveQuestionList(examId: String, questionList: List<QuestionDto>) {
        questionList.forEach { question ->
            QuestionEntity(
                questionId = question.id,
                hostExamId = examId,
                question = question.question,
                type = question.type,
                score = question.score,
                orderNumber = question.orderNumber,
                createdAt = Date().time,
                updatedAt = Date().time,
            ).also { questionDao.insertQuestions(it) }

            saveAnswerList(question.id, question.answers)
        }
    }

    private suspend fun saveAnswerList(questionId: String, answerList: List<AnswerDto>) {
        answerList.forEach { answer ->
            AnswerEntity(
                answerId = answer.id,
                hostQuestionId = questionId,
                answer = answer.answer,
                orderNumber = answer.orderNumber,
                isCorrect = answer.isCorrect,
                createdAt = Date().time,
                updatedAt = Date().time,
            ).also { answerDao.insertAnswers(it) }
        }
    }
}