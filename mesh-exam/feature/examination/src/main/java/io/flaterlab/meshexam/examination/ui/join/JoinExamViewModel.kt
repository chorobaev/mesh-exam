package io.flaterlab.meshexam.examination.ui.join

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.flaterlab.meshexam.androidbase.BaseViewModel
import io.flaterlab.meshexam.androidbase.SingleLiveEvent
import io.flaterlab.meshexam.androidbase.getLauncher
import io.flaterlab.meshexam.domain.interactor.ExaminationInteractor
import io.flaterlab.meshexam.examination.ExamLauncher
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class JoinExamViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val examinationInteractor: ExaminationInteractor,
) : BaseViewModel() {

    private val launcher: ExamLauncher = savedStateHandle.getLauncher()

    val commandConnected = SingleLiveEvent<String>()
    val commandConnectionFailed = SingleLiveEvent<Unit>()

    init {
        joinExam()
    }

    private fun joinExam() {
        viewModelScope.launch {
            try {
                examinationInteractor.joinExam(launcher.examId)
                commandConnected.value = launcher.examId
            } catch (e: Exception) {
                e.showLocalizedMessage()
                commandConnectionFailed.call()
            }
        }
    }
}