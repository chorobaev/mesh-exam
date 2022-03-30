package io.flaterlab.meshexam.result.ui.question

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.flaterlab.meshexam.androidbase.BaseViewModel
import io.flaterlab.meshexam.androidbase.getLauncher
import io.flaterlab.meshexam.domain.interactor.ProfileInteractor
import io.flaterlab.meshexam.result.dvo.ResultAnswerDvo
import io.flaterlab.meshexam.result.dvo.ResultQuestionDvo
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
internal class ResultQuestionViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    profileInteractor: ProfileInteractor,
) : BaseViewModel() {

    private val launcher: ResultQuestionLauncher = savedStateHandle.getLauncher()

    val question = profileInteractor.questionResult(launcher.questionId, launcher.attemptId)
        .map { questionModel ->
            ResultQuestionDvo(
                questionId = questionModel.questionId,
                question = questionModel.question,
                answers = questionModel.answerResultList
                    .map { answerModel ->
                        ResultAnswerDvo(
                            answerId = answerModel.answerId,
                            answer = answerModel.answer,
                            isCorrect = answerModel.isCorrect,
                            isSelected = answerModel.isSelected,
                        )
                    }
            )
        }
        .catch { e -> e.showLocalizedMessage() }
        .asLiveData(viewModelScope.coroutineContext)
}