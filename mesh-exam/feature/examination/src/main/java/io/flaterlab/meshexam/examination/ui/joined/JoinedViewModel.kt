package io.flaterlab.meshexam.examination.ui.joined

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.flaterlab.meshexam.androidbase.BaseViewModel
import io.flaterlab.meshexam.androidbase.SingleLiveEvent
import io.flaterlab.meshexam.androidbase.getLauncher
import io.flaterlab.meshexam.domain.exam.model.ExamStateModel
import io.flaterlab.meshexam.domain.interactor.ExaminationInteractor
import io.flaterlab.meshexam.examination.ExamLauncher
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class JoinedViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val examInteractor: ExaminationInteractor,
) : BaseViewModel() {

    private val launcher: ExamLauncher = savedStateHandle.getLauncher()

    val examName = MutableLiveData<String>()

    val commandExamStarted = SingleLiveEvent<String>()
    val commandShowLeavePrompt = SingleLiveEvent<Unit>()
    val commandLeaveExam = SingleLiveEvent<Unit>()

    init {
        examInteractor.examState(launcher.examId)
            .onEach { examState ->
                when (examState) {
                    is ExamStateModel.Started -> commandExamStarted.value = examState.examId
                    is ExamStateModel.Waiting -> examName.value = examState.examName
                }
            }
            .launchIn(viewModelScope)
    }

    fun onBackPressed() {
        commandShowLeavePrompt.call()
    }

    fun onLeaveClicked() {
        viewModelScope.launch {
            examInteractor.leaveExam(launcher.examId)
            commandLeaveExam.call()
        }
    }
}