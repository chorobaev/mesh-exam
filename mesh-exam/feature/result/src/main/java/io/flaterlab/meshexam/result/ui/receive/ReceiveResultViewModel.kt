package io.flaterlab.meshexam.result.ui.receive

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.flaterlab.meshexam.androidbase.BaseViewModel
import io.flaterlab.meshexam.androidbase.getLauncher
import io.flaterlab.meshexam.androidbase.text.Text
import io.flaterlab.meshexam.core.DateUtils
import io.flaterlab.meshexam.domain.interactor.ProfileInteractor
import io.flaterlab.meshexam.domain.interactor.ResultsInteractor
import io.flaterlab.meshexam.result.dvo.HostExamInfoDvo
import io.flaterlab.meshexam.result.dvo.ResultItemDvo
import io.flaterlab.meshexam.uikit.view.StateRecyclerView
import io.flaterlab.meshexam.uikit.view.toEmptinessListState
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject
import kotlin.math.roundToInt

@HiltViewModel
internal class ReceiveResultViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    profileInteractor: ProfileInteractor,
    private val resultsInteractor: ResultsInteractor,
) : BaseViewModel() {

    private val launcher: ReceiveResultLauncher = savedStateHandle.getLauncher()

    val examInfo = profileInteractor.hostingResultMeta(launcher.hostingId)
        .map { metaModel ->
            HostExamInfoDvo(
                examName = metaModel.examName,
                info = metaModel.examInfo,
                duration = DateUtils.formatTimeMmSs(metaModel.durationInMillis),
                submissionCount = metaModel.submissionCount,
                expectedSubmissionCount = metaModel.expectedSubmissionCount,
            )
        }
        .catch { e -> e.showLocalizedMessage() }
        .asLiveData(viewModelScope.coroutineContext)

    val resultListState = MutableLiveData(StateRecyclerView.State.EMPTY)
    val resultList = resultsInteractor.startAttemptResultAccepting(launcher.hostingId)
        .onEach { resultListState.value = it.toEmptinessListState() }
        .map { list ->
            list.map { model ->
                ResultItemDvo(
                    id = model.id,
                    fullName = model.studentFullName,
                    info = Text.from(model.studentInfo),
                    status = Text.from(
                        model.status.toString()
                            .lowercase()
                            .replaceFirstChar { it.uppercase() }
                    ),
                    grade = model.grade.roundToInt(),
                    totalGrade = model.totalGrade,
                )
            }
        }
        .catch { it.showLocalizedMessage() }
        .asLiveData(viewModelScope.coroutineContext)

    fun onResultClicked(dvo: ResultItemDvo) = Unit
}