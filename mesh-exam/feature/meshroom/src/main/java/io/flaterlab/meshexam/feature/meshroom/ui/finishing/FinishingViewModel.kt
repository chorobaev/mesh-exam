package io.flaterlab.meshexam.feature.meshroom.ui.finishing

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.flaterlab.meshexam.androidbase.BaseViewModel
import io.flaterlab.meshexam.androidbase.SingleLiveEvent
import io.flaterlab.meshexam.androidbase.getLauncher
import io.flaterlab.meshexam.androidbase.text.Text
import io.flaterlab.meshexam.domain.interactor.MeshInteractor
import io.flaterlab.meshexam.domain.mesh.model.HostedStudentModel
import io.flaterlab.meshexam.feature.meshroom.R
import io.flaterlab.meshexam.feature.meshroom.dvo.SubmissionDvo
import io.flaterlab.meshexam.feature.meshroom.ui.result.HostResultLauncher
import io.flaterlab.meshexam.uikit.view.StateRecyclerView
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
internal class FinishingViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    meshInteractor: MeshInteractor,
) : BaseViewModel() {

    private val launcher: FinishingLauncher = savedStateHandle.getLauncher()

    private val searchText = MutableStateFlow<String?>(null)

    @OptIn(ExperimentalCoroutinesApi::class)
    val submissionList = searchText
        .flatMapLatest { name ->
            meshInteractor.hostedStudentList(launcher.hostingId, name)
                .onEach { list ->
                    if (name.isNullOrBlank()) {
                        submissionAmount.value = Pair(
                            list.count { it.status == HostedStudentModel.Status.SUBMITTED },
                            list.size
                        )
                    }
                }
        }
        .map { list ->
            list.map { studentModel ->
                SubmissionDvo(
                    id = studentModel.userId,
                    fullName = studentModel.fullName,
                    info = Text.from(studentModel.info),
                    status = Text.from(
                        when (studentModel.status) {
                            HostedStudentModel.Status.ATTEMPTING ->
                                R.string.monitor_finishing_statusSubmitting
                            HostedStudentModel.Status.SUBMITTED ->
                                R.string.monitor_finishing_statusSubmitted
                        }
                    ),
                    statusColorResId = when (studentModel.status) {
                        HostedStudentModel.Status.ATTEMPTING -> R.color.gray_2
                        HostedStudentModel.Status.SUBMITTED -> R.color.purple_500
                    }
                )
            }
        }
        .onEach { list ->
            submissionListStat.value = if (list.isEmpty()) {
                StateRecyclerView.State.EMPTY
            } else {
                StateRecyclerView.State.NORMAL
            }
        }
        .catch { e -> e.showLocalizedMessage() }
        .asLiveData(viewModelScope.coroutineContext)

    val submissionListStat = MutableLiveData(StateRecyclerView.State.NORMAL)
    val submissionAmount = MutableLiveData<Pair<Int, Int>>()

    val commandConfirmFinish = SingleLiveEvent<Unit>()
    val commandOpenResult = SingleLiveEvent<HostResultLauncher>()

    fun onFinishImmediatelyClicked() {
        commandConfirmFinish.call()
    }

    fun onFinishImmediatelyConfirmed() {
        commandOpenResult.value = HostResultLauncher(launcher.hostingId)
    }

    fun onBackPressed() {
        commandConfirmFinish.call()
    }

    fun onSearchTextChanged(text: String?) {
        searchText.value = text
    }
}