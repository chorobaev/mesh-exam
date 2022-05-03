package io.flaterlab.meshexam.domain.impl

import io.flaterlab.meshexam.domain.create.model.ExamInfoModel
import io.flaterlab.meshexam.domain.create.model.QuestionModel
import io.flaterlab.meshexam.domain.exam.model.*
import io.flaterlab.meshexam.domain.interactor.ExaminationInteractor
import io.flaterlab.meshexam.domain.repository.AttemptRepository
import io.flaterlab.meshexam.domain.repository.DiscoveryRepository
import io.flaterlab.meshexam.domain.repository.ExamRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ExaminationInteractorImpl @Inject constructor(
    private val discoveryRepository: DiscoveryRepository,
    private val attemptRepository: AttemptRepository,
    private val examRepository: ExamRepository,
) : ExaminationInteractor {

    override fun discoverExams(): Flow<List<ExamInfoModel>> {
        return discoveryRepository.discoverExams()
    }

    override suspend fun joinExam(examId: String) {
        discoveryRepository.joinExam(examId)
    }

    override fun examState(examId: String): Flow<ExamStateModel> {
        return discoveryRepository.examState(examId)
    }

    override suspend fun leaveExam(examId: String) {
        discoveryRepository.leaveExam(examId)
    }

    override suspend fun finishAttempt(attemptId: String) {
        attemptRepository.finishAttempt(attemptId)
    }

    override fun attemptMetaById(attemptId: String): Flow<AttemptMetaModel> {
        return attemptRepository.attemptMetaById(attemptId)
    }

    override fun attemptTimeLeftInSec(attemptId: String): Flow<Int> {
        return attemptRepository.attemptTimeLeftInSec(attemptId)
    }

    override fun questionIdsByExamId(examId: String): Flow<List<String>> {
        return examRepository
            .examWithQuestionIdsByExamId(examId)
            .map { it.questionIds }
    }

    override fun questionById(questionId: String): Flow<QuestionModel> {
        return examRepository.questionById(questionId)
    }

    override fun answersByAttemptAndQuestionId(
        attemptId: String,
        questionId: String,
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

    override suspend fun sendExamEvent(attemptId: String, event: ExamEvent) {
        attemptRepository.sendExamEvent(attemptId, event)
    }
}