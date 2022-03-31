package io.flaterlab.meshexam.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.flaterlab.meshexam.data.database.entity.host.HostAttemptAnswerEntity

@Dao
internal interface HostAttemptAnswerDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vararg answers: HostAttemptAnswerEntity)

    @Query(
        "SELECT COUNT(answers.answerId) FROM answers JOIN host_attempt_answers ON " +
                "answers.answerId = host_attempt_answers.answerId WHERE " +
                "host_attempt_answers.attemptId = :attemptId AND answers.isCorrect = 1"
    )
    suspend fun getCorrectAnswerCountByAttemptId(attemptId: String): Int

    @Query(
        "SELECT answers.isCorrect FROM answers JOIN host_attempt_answers ON " +
                "answers.answerId = host_attempt_answers.answerId WHERE " +
                "host_attempt_answers.questionId = :questionId AND " +
                "host_attempt_answers.attemptId = :attemptId"
    )
    suspend fun isQuestionAnsweredCorrectly(questionId: String, attemptId: String): Boolean?

    @Query(
        "SELECT host_attempt_answers.createdAt FROM answers JOIN host_attempt_answers ON " +
                "answers.answerId = host_attempt_answers.answerId WHERE " +
                "answers.answerId = :answerId AND host_attempt_answers.attemptId = :attemptId"
    )
    suspend fun getAnswerSelectedTime(answerId: String, attemptId: String): Long?
}