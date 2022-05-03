package io.flaterlab.meshexam.data.database.dao

import androidx.room.*
import io.flaterlab.meshexam.data.database.entity.ExamEntity
import io.flaterlab.meshexam.data.database.entity.client.ExamToHostingMapperEntity
import kotlinx.coroutines.flow.Flow

@Dao
internal interface ExamDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExams(vararg exams: ExamEntity)

    @Update(entity = ExamEntity::class)
    suspend fun updateExam(exam: ExamEntity)

    @Query("DELETE FROM exams WHERE examId = :examId")
    suspend fun deleteExam(examId: String)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExamToHostingMapper(vararg mapper: ExamToHostingMapperEntity)

    @Query("SELECT * FROM exams WHERE hostUserId = :userId")
    fun getExams(userId: String): Flow<List<ExamEntity>>

    @Query("SELECT * FROM exams WHERE examId = :examId")
    suspend fun getExamById(examId: String): ExamEntity

    @Query("SELECT name FROM exams WHERE examId = :examId")
    suspend fun getExamNameById(examId: String): String

    @Query("SELECT hostingId FROM exam_to_hosting_mapper WHERE examId = :examId")
    suspend fun getHostingIdByExamId(examId: String): String

    @Query("SELECT SUM(score) FROM questions WHERE hostExamId = :examId")
    suspend fun getTotalScoreByExamId(examId: String): Float

    @Query("SELECT * FROM exams WHERE examId = :examId")
    fun examById(examId: String): Flow<ExamEntity>
}