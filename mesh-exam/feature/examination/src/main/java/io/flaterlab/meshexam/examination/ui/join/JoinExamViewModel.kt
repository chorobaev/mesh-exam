package io.flaterlab.meshexam.examination.ui.join

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.flaterlab.meshexam.androidbase.BaseViewModel
import io.flaterlab.meshexam.androidbase.SingleLiveEvent
import io.flaterlab.meshexam.androidbase.getLauncher
import io.flaterlab.meshexam.examination.ExamLauncher
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class JoinExamViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
) : BaseViewModel() {

    private val launcher: ExamLauncher = savedStateHandle.getLauncher()

    val commandConnected = SingleLiveEvent<Unit>()

    init {
        viewModelScope.launch {
            delay(2000)
            commandConnected.call()
        }
    }
}