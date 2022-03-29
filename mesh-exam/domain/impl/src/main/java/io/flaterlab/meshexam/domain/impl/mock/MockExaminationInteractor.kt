package io.flaterlab.meshexam.domain.impl.mock

import io.flaterlab.meshexam.domain.create.model.ExamInfoModel
import io.flaterlab.meshexam.domain.create.model.QuestionModel
import io.flaterlab.meshexam.domain.exam.model.*
import io.flaterlab.meshexam.domain.interactor.ExaminationInteractor
import io.flaterlab.meshexam.domain.repository.AttemptRepository
import io.flaterlab.meshexam.domain.repository.ExamRepository
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class MockExaminationInteractor @Inject constructor(
    private val attemptRepository: AttemptRepository,
    private val examRepository: ExamRepository,
) : ExaminationInteractor {

    override fun discoverExams(): Flow<List<ExamInfoModel>> {
        return flowOf((1..5))
            .map { range ->
                range.map {
                    ExamInfoModel(
                        "ba121a9e-cc56-4e80-a492-6243355fe888",
                        "Exam #$it",
                        "Mock User #$it",
                        20
                    )
                }
            }
    }

    override suspend fun joinExam(examId: String) {
        println("Joining exam")
    }

    override fun examState(examId: String): Flow<ExamStateModel> {
        return flow {
            val mockId = examRepository.exams().firstOrNull()?.firstOrNull()?.id.orEmpty()
            emit(
                ExamStateModel.Started(
                    examId = mockId,
                    attemptId = attemptRepository.attempt(mockId),
                )
            )
        }
    }

    override suspend fun leaveExam(examId: String) {
        println("Leaving exam")
    }

    override suspend fun finishAttempt(attemptId: String) {
        attemptRepository.finishAttempt(attemptId)
    }

    override fun attemptMetaById(attemptId: String): Flow<AttemptMetaModel> {
        return attemptRepository.attemptMetaById(attemptId)
    }

    override fun attemptTimeLeftInSec(attemptId: String): Flow<Int> {
        TODO("Implement according to need")
    }

    override fun questionIdsByExamId(examId: String): Flow<List<String>> {
        return flow {
            emit(
                examRepository
                    .getExamWithQuestionIdsByExamId(examId)
                    .questionIds
            )
        }
    }

    override fun questionById(questionId: String): Flow<QuestionModel> {
        return examRepository.questionById(questionId)
    }

    override fun answersByAttemptAndQuestionId(
        attemptId: String,
        questionId: String
    ): Flow<List<ExamAnswerModel>> {
        return combine(
            examRepository.answersByQuestionId(questionId),
            attemptRepository.selectedAnswerByAttemptAndQuestionId(attemptId, questionId),
        ) { answers, selected ->
            answers.map { answer ->
                ExamAnswerModel(
                    id = answer.id,
                    content = answer.content,
                    isSelected = answer.id == selected.answerId
                )
            }
        }
    }

    override suspend fun selectAnswer(model: SelectAnswerModel) {
        attemptRepository.addAttemptAnswer(model)
    }

    override fun attemptResult(attemptId: String): Flow<AttemptResultModel> {
        return attemptRepository.attemptResult(attemptId)
    }
}