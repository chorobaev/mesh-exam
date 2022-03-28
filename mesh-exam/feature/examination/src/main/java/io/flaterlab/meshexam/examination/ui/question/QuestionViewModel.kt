package io.flaterlab.meshexam.examination.ui.question

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.flaterlab.meshexam.androidbase.BaseViewModel
import io.flaterlab.meshexam.androidbase.getLauncher
import io.flaterlab.meshexam.domain.exam.model.SelectAnswerModel
import io.flaterlab.meshexam.domain.interactor.ExaminationInteractor
import io.flaterlab.meshexam.examination.dvo.AnswerDvo
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class QuestionViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val examInteractor: ExaminationInteractor,
) : BaseViewModel() {

    private val launcher: QuestionLauncher = savedStateHandle.getLauncher()

    val question = examInteractor
        .questionById(launcher.questionId)
        .map { it.content }
        .asLiveData(viewModelScope.coroutineContext)
    val answers = examInteractor
        .answersByAttemptAndQuestionId(launcher.attemptId, launcher.questionId)
        .map { list ->
            list.map { model ->
                AnswerDvo(model.id, model.content, model.isSelected)
            }
        }
        .asLiveData(viewModelScope.coroutineContext)

    fun onAnswerClicked(dvo: AnswerDvo) {
        viewModelScope.launch {
            try {
                examInteractor.selectAnswer(
                    SelectAnswerModel(
                        attemptId = launcher.attemptId,
                        questionId = launcher.questionId,
                        answerId = dvo.id,
                    )
                )
            } catch (ex: Exception) {
                ex.showLocalizedMessage()
            }
        }
    }
}