package io.flaterlab.meshexam.data.database.entity.host

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "exam_events")
internal data class ExamEventEntity(
    @PrimaryKey val eventId: String,
    val hostingId: String,
    val authorClientId: String,
    val eventType: Int,
    val createdAt: Long,
)