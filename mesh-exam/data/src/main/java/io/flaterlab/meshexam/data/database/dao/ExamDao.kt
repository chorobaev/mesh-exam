package io.flaterlab.meshexam.data.database.dao

import androidx.room.*
import io.flaterlab.meshexam.data.database.entity.AnswerEntity
import io.flaterlab.meshexam.data.database.entity.ExamEntity
import io.flaterlab.meshexam.data.database.entity.QuestionEntity
import kotlinx.coroutines.flow.Flow

@Dao
internal interface ExamDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExams(vararg exams: ExamEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuestions(vararg question: QuestionEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAnswers(vararg answers: AnswerEntity)

    @Query("SELECT * FROM exams")
    fun getExams(): Flow<List<ExamEntity>>

    @Query("SELECT * FROM exams WHERE examId = :examId")
    suspend fun getExamById(examId: String): ExamEntity

    @Query("SELECT questionId FROM questions WHERE hostExamId = :examId")
    suspend fun getQuestionIdsByExamId(examId: String): List<String>

    @Query("SELECT * FROM questions WHERE hostExamId = :examId")
    suspend fun getQuestionsByExamId(examId: String): List<QuestionEntity>

    @Query("SELECT * FROM answers WHERE hostQuestionId = :questionId")
    suspend fun getAnswersByQuestionId(questionId: String): List<AnswerEntity>

    @Query("DELETE FROM questions WHERE questionId IN (:questionIds)")
    suspend fun deleteQuestions(vararg questionIds: String)

}