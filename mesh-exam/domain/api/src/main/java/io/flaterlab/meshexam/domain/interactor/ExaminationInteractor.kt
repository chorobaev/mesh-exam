package io.flaterlab.meshexam.domain.interactor

import io.flaterlab.meshexam.domain.create.model.ExamInfoModel
import io.flaterlab.meshexam.domain.create.model.QuestionModel
import io.flaterlab.meshexam.domain.exam.model.ExamAnswerModel
import io.flaterlab.meshexam.domain.exam.model.AttemptMetaModel
import io.flaterlab.meshexam.domain.exam.model.ExamStateModel
import io.flaterlab.meshexam.domain.exam.model.SelectAnswerModel
import kotlinx.coroutines.flow.Flow

interface ExaminationInteractor {

    fun discoverExams(): Flow<List<ExamInfoModel>>

    suspend fun joinExam(examId: String)

    fun examState(examId: String): Flow<ExamStateModel>

    suspend fun leaveExam(examId: String)

    suspend fun finishAttempt(attemptId: String)

    suspend fun getAttemptById(attemptId: String): AttemptMetaModel

    fun attemptTimeLeftInSec(attemptId: String): Flow<Int>

    fun questionIdsByExamId(examId: String): Flow<List<String>>

    fun questionById(questionId: String): Flow<QuestionModel>

    fun answersByQuestionId(questionId: String): Flow<List<ExamAnswerModel>>

    suspend fun selectAnswer(model: SelectAnswerModel)
}