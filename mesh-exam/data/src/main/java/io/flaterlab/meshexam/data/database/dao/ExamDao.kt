package io.flaterlab.meshexam.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
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

    @Query("SELECT * FROM questions WHERE hostExamId = :examId")
    suspend fun getQuestionsByExamId(examId: String): List<QuestionEntity>

    @Query("SELECT * FROM answers WHERE hostQuestionId = :questionId")
    suspend fun getAnswersByQuestionId(questionId: String): List<AnswerEntity>
}