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

    @Query("SELECT * FROM attempt_answers WHERE attemptId = :attemptId AND questionId = :questionId")
    fun attemptAnswerByAttemptAndQuestionId(
        attemptId: String,
        questionId: String,
    ): Flow<AttemptAnswerEntity?>

    @Query("SELECT * FROM attempt_answers WHERE attemptId = :attemptId")
    suspend fun getAnswersByAttemptId(attemptId: String): List<AttemptAnswerEntity>

    @Query(
        "SELECT attempt_answers.createdAt FROM answers JOIN attempt_answers ON " +
                "answers.answerId = attempt_answers.answerId WHERE " +
                "answers.answerId = :answerId AND attempt_answers.attemptId = :attemptId"
    )
    suspend fun getAnswerSelectedTime(answerId: String, attemptId: String): Long?
}