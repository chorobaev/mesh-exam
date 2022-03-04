package io.flaterlab.meshexam.feature.meshroom.ui.result

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import dagger.hilt.android.lifecycle.HiltViewModel
import io.flaterlab.meshexam.androidbase.BaseViewModel
import io.flaterlab.meshexam.androidbase.SingleLiveEvent
import io.flaterlab.meshexam.androidbase.getLauncher
import io.flaterlab.meshexam.feature.meshroom.dvo.HostResultDvo
import javax.inject.Inject

@HiltViewModel
internal class HostResultViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
): BaseViewModel()  {

    private val launcher: HostResultLauncher = savedStateHandle.getLauncher()

    val resultInfo = MutableLiveData<HostResultDvo>()

    val commandOpenResults = SingleLiveEvent<String>()
    val commandGoToMain = SingleLiveEvent<Unit>()

    init {
        // TODO: add actual implementation
        resultInfo.value = HostResultDvo("30:00", 27)
    }

    fun onSeeResultsClicked() {
        commandOpenResults.value = launcher.attemptId
    }

    fun onGoToMainClicked() {
        commandGoToMain.call()
    }
}