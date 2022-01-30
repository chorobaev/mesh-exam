package io.flaterlab.meshexam.data.datasource

import androidx.room.withTransaction
import io.flaterlab.meshexam.core.Mapper
import io.flaterlab.meshexam.data.database.MeshDatabase
import io.flaterlab.meshexam.data.database.entity.ExamEntity
import io.flaterlab.meshexam.data.database.entity.QuestionEntity
import io.flaterlab.meshexam.domain.api.model.CreateExamModel
import io.flaterlab.meshexam.domain.api.model.ExamModel
import io.flaterlab.meshexam.domain.model.ExamWithQuestionIdsModel
import io.flaterlab.meshexam.domain.datasource.ExamDataSource
import io.flaterlab.meshexam.domain.model.CreateQuestionModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

internal class ExamDataSourceImpl @Inject constructor(
    private val database: MeshDatabase,
    private val createExamModelMapper: Mapper<CreateExamModel, ExamEntity>,
    private val examEntityMapper: Mapper<ExamEntity, ExamModel>,
    private val createQuestionModelMapper: Mapper<CreateQuestionModel, QuestionEntity>,
) : ExamDataSource {

    private val examDao = database.getExamsDao()

    override suspend fun createExam(createExam: CreateExamModel): String {
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

    override suspend fun createQuestion(model: CreateQuestionModel): String {
        val question = createQuestionModelMapper(model)
        examDao.insertQuestions(question)
        return question.questionId
    }

    override suspend fun deleteQuestions(vararg questionIds: String) {
        examDao.deleteQuestions(*questionIds)
    }
}