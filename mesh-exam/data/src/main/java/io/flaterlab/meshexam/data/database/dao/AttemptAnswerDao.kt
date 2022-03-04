package io.flaterlab.meshexam.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.flaterlab.meshexam.data.database.entity.AttemptAnswerEntity
import kotlinx.coroutines.flow.Flow

@Dao
internal interface AttemptAnswerDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vararg answers: AttemptAnswerEntity)

    @Query("SELECT * FROM attempt_answers WHERE questionId = :questionId")
    fun attemptAnswerByQuestionId(questionId: String): Flow<AttemptAnswerEntity?>
}