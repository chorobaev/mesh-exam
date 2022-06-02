package io.flaterlab.meshexam.result.ui.result

import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import io.flaterlab.meshexam.androidbase.BaseViewModel
import io.flaterlab.meshexam.androidbase.SingleLiveEvent
import io.flaterlab.meshexam.androidbase.getLauncher
import io.flaterlab.meshexam.androidbase.text.Text
import io.flaterlab.meshexam.core.DateUtils
import io.flaterlab.meshexam.domain.interactor.ProfileInteractor
import io.flaterlab.meshexam.result.ClientResultLauncher
import io.flaterlab.meshexam.result.R
import io.flaterlab.meshexam.result.dvo.IndividualExamInfoDvo
import io.flaterlab.meshexam.result.dvo.ResultQuestionInfoDvo
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
internal class IndividualResultViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    profileInteractor: ProfileInteractor,
) : BaseViewModel() {

    private val launcher: ClientResultLauncher = savedStateHandle.getLauncher()

    val metaInfo = profileInteractor.individualResultMeta(launcher.id)
        .map { resultModel ->
            IndividualExamInfoDvo(
                examId = resultModel.examId,
                name = resultModel.examName,
                info = resultModel.examInfo,
                generalInfoList = buildList {
                    add(
                        Pair(
                            Text.from(R.string.result_details_timeTitle),
                            Text.from(DateUtils.formatTimeMmSs(resultModel.durationInMillis))
                        )
                    )
                    add(
                        Pair(
                            Text.from(R.string.result_details_totalQuestionsTitle),
                            Text.from(resultModel.totalQuestionsCount.toString())
                        )
                    )
                    if (launcher.showCorrectness) {
                        add(
                            Pair(
                                Text.from(R.string.result_details_correctAnswersTitle),
                                Text.from(resultModel.correctAnswers.toString())
                            )
                        )
                    }
                },
                questionInfoList = resultModel.questionInfoList
                    .map { questionInfoModel ->
                        ResultQuestionInfoDvo(
                            questionId = questionInfoModel.questionId,
                            attemptId = launcher.id,
                            isCorrect = questionInfoModel.isCorrect || !launcher.showCorrectness,
                            showCorrectness = launcher.showCorrectness,
                        )
                    }
            )
        }
        .catch { e -> e.showLocalizedMessage() }
        .asLiveData(viewModelScope.coroutineContext)

    val sendingEnabled: LiveData<Boolean> = MutableLiveData(!launcher.showCorrectness)
    val commandSendResult = SingleLiveEvent<String>()

    fun onSendResultClicked() {
        commandSendResult.value = launcher.id
    }
}