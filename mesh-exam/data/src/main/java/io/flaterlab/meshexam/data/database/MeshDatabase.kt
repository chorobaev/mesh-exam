package io.flaterlab.meshexam.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import io.flaterlab.meshexam.data.database.dao.AnswerDao
import io.flaterlab.meshexam.data.database.dao.ExamDao
import io.flaterlab.meshexam.data.database.dao.QuestionDao
import io.flaterlab.meshexam.data.database.entity.AnswerEntity
import io.flaterlab.meshexam.data.database.entity.ExamEntity
import io.flaterlab.meshexam.data.database.entity.QuestionEntity
import io.flaterlab.meshexam.data.worker.SeedDatabaseWorker
import io.flaterlab.meshexam.data.worker.SeedDatabaseWorker.Companion.KEY_FILE_NAME

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

    abstract fun examDao(): ExamDao

    abstract fun questionsDao(): QuestionDao

    abstract fun answerDao(): AnswerDao

    companion object {
        @Volatile
        private var instance: MeshDatabase? = null

        fun getInstance(context: Context): MeshDatabase {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context)
                    .also { instance = it }
            }
        }

        private fun buildDatabase(context: Context): MeshDatabase {
            return Room
                .databaseBuilder(context, MeshDatabase::class.java, "mesh_db")
                .addCallback(
                    object : RoomDatabase.Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                            val request = OneTimeWorkRequestBuilder<SeedDatabaseWorker>()
                                .setInputData(workDataOf(KEY_FILE_NAME to "test_exam.json"))
                                .build()
                            WorkManager.getInstance(context).enqueue(request)
                        }
                    }
                )
                .build()
        }
    }
}