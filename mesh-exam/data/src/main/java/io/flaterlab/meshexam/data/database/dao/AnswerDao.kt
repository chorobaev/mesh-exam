package io.flaterlab.meshexam.data.database.dao

import androidx.room.*
import io.flaterlab.meshexam.data.database.entity.AnswerEntity
import io.flaterlab.meshexam.data.database.entity.update.AnswerContent
import io.flaterlab.meshexam.data.database.entity.update.AnswerCorrectness
import kotlinx.coroutines.flow.Flow

@Dao
internal interface AnswerDao {

    @Query("SELECT * FROM answers WHERE hostQuestionId = :questionId")
    fun answersByQuestionId(questionId: String): Flow<List<AnswerEntity>>

    @Query("SELECT * FROM answers WHERE answerId = :answerId")
    suspend fun getAnswerById(answerId: String): AnswerEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAnswers(vararg answers: AnswerEntity)

    @Query("DELETE FROM answers WHERE answerId IN (:answerIds)")
    suspend fun deleteAnswers(vararg answerIds: String)

    @Update(entity = AnswerEntity::class)
    suspend fun updateAnswerContent(vararg content: AnswerContent)

    @Update(entity = AnswerEntity::class)
    suspend fun updateAnswerCorrectness(vararg list: AnswerCorrectness)
}