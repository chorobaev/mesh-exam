package io.flaterlab.meshexam.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters

@Entity(tableName = "attempts")
@TypeConverters(AttemptStatusConverter::class)
internal data class AttemptEntity(
    @PrimaryKey val attemptId: String,
    val userId: String,
    val examId: String,
    val hostingId: String,
    val status: Status,
    val score: Float?,
    val createdAt: Long,
    val updatedAt: Long,
    val submittedAt: Long?,
) {

    val isFinished get() = submittedAt != null

    enum class Status {
        STARTED,
        FINISHED,
        ;
    }
}

internal class AttemptStatusConverter {

    @TypeConverter
    fun toAttemptStatus(value: String): AttemptEntity.Status {
        return enumValueOf(value)
    }

    @TypeConverter
    fun fromAttemptStatus(value: AttemptEntity.Status): String {
        return value.toString()
    }
}