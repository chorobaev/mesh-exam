package io.flaterlab.meshexam.examination.ui.result

import androidx.lifecycle.SavedStateHandle
import dagger.hilt.android.lifecycle.HiltViewModel
import io.flaterlab.meshexam.androidbase.BaseViewModel
import io.flaterlab.meshexam.androidbase.SingleLiveEvent
import io.flaterlab.meshexam.androidbase.getLauncher
import javax.inject.Inject

@HiltViewModel
internal class ResultViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
) : BaseViewModel() {

    private val launcher: ResultLauncher = savedStateHandle.getLauncher()

    val commandGoToMain = SingleLiveEvent<Unit>()

    fun onGoMainClicked() {
        commandGoToMain.call()
    }
}