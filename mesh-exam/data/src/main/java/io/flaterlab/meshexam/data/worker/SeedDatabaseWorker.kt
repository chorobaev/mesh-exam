package io.flaterlab.meshexam.data.worker

import android.content.Context
import androidx.room.withTransaction
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.JsonReader
import dagger.hilt.android.EntryPointAccessors
import io.flaterlab.meshexam.data.database.entity.AnswerEntity
import io.flaterlab.meshexam.data.database.entity.ExamEntity
import io.flaterlab.meshexam.data.database.entity.QuestionEntity
import io.flaterlab.meshexam.data.di.WorkerEntryPoint
import io.flaterlab.meshexam.data.strategy.IdGeneratorStrategy
import io.flaterlab.meshexam.data.worker.entity.AnswerJson
import io.flaterlab.meshexam.data.worker.entity.ExamJson
import io.flaterlab.meshexam.data.worker.entity.QuestionJson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.util.*

internal class SeedDatabaseWorker(
    context: Context,
    workerParams: WorkerParameters,
) : CoroutineWorker(context, workerParams) {

    private val entryPoint = EntryPointAccessors
        .fromApplication(context.applicationContext, WorkerEntryPoint::class.java)
    private val userProfileDao = entryPoint.userProfileDao
    private val database = entryPoint.meshDatabase
    private val examDao get() = database.examDao()
    private val questionDao get() = database.questionDao()
    private val answerDao get() = database.answerDao()
    private val idGenerator = IdGeneratorStrategy()

    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        try {
            val fileName = inputData.getString(KEY_FILE_NAME)
            if (fileName != null) {
                applicationContext.assets.open(fileName).use { inputStream ->
                    JsonReader(inputStream.reader()).use { jsonReader ->
                        val examType = object : TypeToken<List<ExamJson>>() {}.type
                        val examList: List<ExamJson> = Gson().fromJson(jsonReader, examType)

                        saveExamList(examList)
                    }
                }

                Result.success()
            } else {
                Timber.e("Filename retrieving error - no valid file name")
                Result.failure()
            }
        } catch (ex: Exception) {
            Timber.e("Database seeding error", ex)
            Result.failure()
        }
    }

    private suspend fun saveExamList(examList: List<ExamJson>) {
        val profile = userProfileDao.userProfile().first()
        database.withTransaction {
            examList.forEach { exam ->
                val newExamId = idGenerator.generate()
                ExamEntity(
                    examId = newExamId,
                    hostUserId = profile.id,
                    name = exam.name,
                    type = exam.type,
                    durationInMin = exam.durationInMin
                ).also { examDao.insertExams(it) }

                saveQuestionList(newExamId, exam.questions)
            }
        }
    }

    private suspend fun saveQuestionList(examId: String, questionList: List<QuestionJson>) {
        questionList.forEach { question ->
            val newQuestionId = idGenerator.generate()
            QuestionEntity(
                questionId = newQuestionId,
                hostExamId = examId,
                question = question.question,
                type = question.type,
                score = question.score,
                orderNumber = question.orderNumber,
                createdAt = Date().time,
                updatedAt = Date().time,
            ).also { questionDao.insertQuestions(it) }

            saveAnswerList(newQuestionId, question.answers)
        }
    }

    private suspend fun saveAnswerList(questionId: String, answerList: List<AnswerJson>) {
        answerList.forEach { answer ->
            val newAnswerId = idGenerator.generate()
            AnswerEntity(
                answerId = newAnswerId,
                hostQuestionId = questionId,
                answer = answer.answer,
                orderNumber = answer.orderNumber,
                isCorrect = answer.isCorrect,
                createdAt = Date().time,
                updatedAt = Date().time,
            ).also { answerDao.insertAnswers(it) }
        }
    }

    companion object {
        const val KEY_FILE_NAME = "EXAMS_FILE_NAME"
    }
}