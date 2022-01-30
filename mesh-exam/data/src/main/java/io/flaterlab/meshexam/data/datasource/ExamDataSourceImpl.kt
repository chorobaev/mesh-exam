package io.flaterlab.meshexam.data.datasource

import androidx.room.withTransaction
import io.flaterlab.meshexam.core.Mapper
import io.flaterlab.meshexam.data.database.MeshDatabase
import io.flaterlab.meshexam.data.database.entity.ExamEntity
import io.flaterlab.meshexam.domain.api.datasource.ExamDataSource
import io.flaterlab.meshexam.domain.api.model.CreateExamModel
import io.flaterlab.meshexam.domain.api.model.ExamModel
import io.flaterlab.meshexam.domain.api.model.ExamWithQuestionIdsModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

internal class ExamDataSourceImpl @Inject constructor(
    private val database: MeshDatabase,
    private val createExamModelMapper: Mapper<CreateExamModel, ExamEntity>,
    private val examEntityMapper: Mapper<ExamEntity, ExamModel>
) : ExamDataSource {

    private val examDao = database.getExamsDao()

    override suspend fun createTest(createExam: CreateExamModel): String {
        val entity = createExamModelMapper(createExam)
        examDao.insertExams(entity)
        return entity.examId
    }

    override fun exams(): Flow<List<ExamModel>> {
        return examDao.getExams().map { it.map(examEntityMapper::invoke) }
    }

    override suspend fun getExamWithQuestionIdsByExamId(examId: String): ExamWithQuestionIdsModel {
        return database.withTransaction {
            val exam = examDao.getExamById(examId)
            val questionIds = examDao.getQuestionIdsByExamId(examId)
            ExamWithQuestionIdsModel(
                exam = examEntityMapper(exam),
                questionIds = questionIds
            )
        }
    }
}