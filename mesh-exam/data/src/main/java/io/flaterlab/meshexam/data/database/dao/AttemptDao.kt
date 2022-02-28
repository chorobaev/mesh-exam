package io.flaterlab.meshexam.data.database.dao

import androidx.room.*
import io.flaterlab.meshexam.data.database.entity.AttemptEntity
import io.flaterlab.meshexam.data.database.entity.update.AttemptFinishing

@Dao
internal interface AttemptDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vararg attempts: AttemptEntity)

    @Query("SELECT * FROM attempts WHERE attemptId = :attemptId")
    suspend fun getAttemptById(attemptId: String): AttemptEntity

    @Query("SELECT * FROM attempts WHERE submittedAt is null")
    suspend fun getActiveAttempts(): List<AttemptEntity>

    @Update(entity = AttemptEntity::class)
    suspend fun updateToFinishAttempt(vararg attemptFinishing: AttemptFinishing)
}