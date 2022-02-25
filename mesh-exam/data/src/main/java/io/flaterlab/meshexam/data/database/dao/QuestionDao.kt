package io.flaterlab.meshexam.data.database.dao

import androidx.room.*
import io.flaterlab.meshexam.data.database.entity.QuestionEntity
import io.flaterlab.meshexam.data.database.entity.update.QuestionContent
import kotlinx.coroutines.flow.Flow

@Dao
internal interface QuestionDao {

    @Query("SELECT * FROM questions WHERE questionId = :questionId")
    fun questionById(questionId: String): Flow<QuestionEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuestions(vararg question: QuestionEntity)

    @Query("SELECT questionId FROM questions WHERE hostExamId = :examId ORDER BY orderNumber")
    suspend fun getQuestionIdsByExamId(examId: String): List<String>

    @Query("SELECT * FROM questions WHERE hostExamId = :examId")
    suspend fun getQuestionsByExamId(examId: String): List<QuestionEntity>

    @Query("DELETE FROM questions WHERE questionId IN (:questionIds)")
    suspend fun deleteQuestions(vararg questionIds: String)

    @Update(entity = QuestionEntity::class)
    suspend fun updateQuestionContents(vararg contents: QuestionContent)
}