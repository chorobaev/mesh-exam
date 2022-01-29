package io.flaterlab.meshexam.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import io.flaterlab.meshexam.data.database.dao.ExamDao
import io.flaterlab.meshexam.data.database.entity.AnswerEntity
import io.flaterlab.meshexam.data.database.entity.ExamEntity
import io.flaterlab.meshexam.data.database.entity.QuestionEntity

@Database(
    entities = [
        ExamEntity::class,
        QuestionEntity::class,
        AnswerEntity::class,
    ],
    version = 1,
    exportSchema = false,
)
internal abstract class MeshDatabase : RoomDatabase() {

    abstract fun getExamsDao(): ExamDao
}