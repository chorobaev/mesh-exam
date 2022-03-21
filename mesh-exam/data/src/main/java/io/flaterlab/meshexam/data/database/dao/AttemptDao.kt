package io.flaterlab.meshexam.data.database.dao

import androidx.room.*
import io.flaterlab.meshexam.data.database.entity.AttemptEntity
import io.flaterlab.meshexam.data.database.entity.host.AttemptToHostingMapperEntity
import io.flaterlab.meshexam.data.database.entity.update.AttemptFinishing

@Dao
internal interface AttemptDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vararg attempts: AttemptEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAttemptToHostingMapper(vararg mapper: AttemptToHostingMapperEntity)

    @Query("SELECT * FROM attempts WHERE attemptId = :attemptId")
    suspend fun getAttemptById(attemptId: String): AttemptEntity

    @Query("SELECT * FROM attempts WHERE submittedAt IS NULL")
    suspend fun getActiveAttempts(): List<AttemptEntity>

    @Query(
        "SELECT * FROM attempts WHERE userId = :userId AND submittedAt IS NOT NULL" +
                " ORDER BY submittedAt DESC"
    )
    suspend fun getAttemptsByUserId(userId: String): List<AttemptEntity>

    @Query("SELECT attemptId FROM attempt_to_hosting_mapper WHERE hostingId = :hostingId")
    suspend fun getAttemptIdsByHostingId(hostingId: String): List<String>

    @Update(entity = AttemptEntity::class)
    suspend fun updateToFinishAttempt(vararg attemptFinishing: AttemptFinishing)
}