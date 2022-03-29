package io.flaterlab.meshexam.data.database.dao

import androidx.room.*
import io.flaterlab.meshexam.data.database.entity.AttemptEntity
import io.flaterlab.meshexam.data.database.entity.update.AttemptFinishing
import kotlinx.coroutines.flow.Flow

@Dao
internal interface AttemptDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vararg attempts: AttemptEntity)

    @Query("SELECT * FROM attempts WHERE attemptId = :attemptId")
    suspend fun getAttemptById(attemptId: String): AttemptEntity

    @Query("SELECT * FROM attempts WHERE submittedAt IS NULL")
    suspend fun getActiveAttempts(): List<AttemptEntity>

    @Query(
        "SELECT * FROM attempts WHERE userId = :userId AND submittedAt IS NOT NULL" +
                " ORDER BY submittedAt DESC"
    )
    suspend fun getAttemptsByUserId(userId: String): List<AttemptEntity>

    @Query("SELECT * FROM attempts WHERE hostingId = :hostingId")
    fun attemptsByHostingId(hostingId: String): Flow<List<AttemptEntity>>

    @Query("SELECT * FROM attempts WHERE attemptId = :attemptId")
    fun attemptById(attemptId: String): Flow<AttemptEntity>

    @Query("SELECT * FROM attempts WHERE hostingId = :hostingId AND userId = :userId")
    suspend fun getAttemptByUserAndHostingId(userId: String, hostingId: String): AttemptEntity?

    @Query("SELECT * FROM attempts WHERE hostingId = :hostingId LIMIT 1")
    suspend fun getClientAttemptByHostingId(hostingId: String): AttemptEntity?

    @Update(entity = AttemptEntity::class)
    suspend fun updateToFinishAttempt(vararg attemptFinishing: AttemptFinishing)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(vararg attempt: AttemptEntity)
}