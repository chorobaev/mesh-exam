package io.flaterlab.meshexam.domain.interactor

import io.flaterlab.meshexam.domain.create.model.ExamInfoModel
import io.flaterlab.meshexam.domain.create.model.QuestionModel
import io.flaterlab.meshexam.domain.exam.model.*
import kotlinx.coroutines.flow.Flow

interface ExaminationInteractor {

    fun discoverExams(): Flow<List<ExamInfoModel>>

    suspend fun joinExam(examId: String)

    fun examState(examId: String): Flow<ExamStateModel>

    suspend fun leaveExam(examId: String)

    suspend fun finishAttempt(attemptId: String)

    fun attemptMetaById(attemptId: String): Flow<AttemptMetaModel>

    fun attemptTimeLeftInSec(attemptId: String): Flow<Int>

    fun questionIdsByExamId(examId: String): Flow<List<String>>

    fun questionById(questionId: String): Flow<QuestionModel>

    fun answersByAttemptAndQuestionId(
        attemptId: String,
        questionId: String
    ): Flow<List<ExamAnswerModel>>

    suspend fun selectAnswer(model: SelectAnswerModel)

    fun attemptResult(attemptId: String): Flow<AttemptResultModel>

    suspend fun sendExamEvent(attemptId: String, event: ExamEvent)
}