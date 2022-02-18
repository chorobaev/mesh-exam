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

    @Query("SELECT * FROM exams WHERE hostUserId = :userId")
    fun getExams(userId: String): Flow<List<ExamEntity>>

    @Query("SELECT * FROM exams WHERE examId = :examId")
    suspend fun getExamById(examId: String): ExamEntity

    @Query("SELECT name FROM exams WHERE examId = :examId")
    suspend fun getExamNameById(examId: String): String
}