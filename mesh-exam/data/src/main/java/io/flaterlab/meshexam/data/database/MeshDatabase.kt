package io.flaterlab.meshexam.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import io.flaterlab.meshexam.data.database.dao.*
import io.flaterlab.meshexam.data.database.entity.*
import io.flaterlab.meshexam.data.worker.SeedDatabaseWorker
import io.flaterlab.meshexam.data.worker.SeedDatabaseWorker.Companion.KEY_FILE_NAME

@Database(
    entities = [
        ExamEntity::class,
        QuestionEntity::class,
        AnswerEntity::class,
        AttemptEntity::class,
        AttemptAnswerEntity::class,
    ],
    version = 1,
    exportSchema = false,
)
internal abstract class MeshDatabase : RoomDatabase() {

    abstract fun examDao(): ExamDao

    abstract fun questionsDao(): QuestionDao

    abstract fun answerDao(): AnswerDao

    abstract fun attemptDao(): AttemptDao

    abstract fun attemptAnswerDao(): AttemptAnswerDao

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