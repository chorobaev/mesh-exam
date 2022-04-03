package io.flaterlab.meshexam.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.flaterlab.meshexam.data.database.entity.host.ExamEventEntity
import kotlinx.coroutines.flow.Flow

@Dao
internal interface ExamEventDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vararg events: ExamEventEntity)

    @Query("SELECT * FROM exam_events WHERE hostingId = :hostingId ORDER BY createdAt DESC")
    fun examEvents(hostingId: String): Flow<List<ExamEventEntity>>
}