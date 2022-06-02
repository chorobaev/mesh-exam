package io.flaterlab.meshexam.result.ui.send

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.flaterlab.meshexam.androidbase.BaseViewModel
import io.flaterlab.meshexam.androidbase.SingleLiveEvent
import io.flaterlab.meshexam.androidbase.getLauncher
import io.flaterlab.meshexam.domain.interactor.ExaminationInteractor
import io.flaterlab.meshexam.domain.interactor.ResultsInteractor
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class SendResultViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    resultsInteractor: ResultsInteractor,
    examInteractor: ExaminationInteractor,
) : BaseViewModel() {

    private val launcher: SendResultLauncher = savedStateHandle.getLauncher()

    val examName = examInteractor.attemptMetaById(launcher.attemptId)
        .map { it.examName }
        .catch { it.showLocalizedMessage() }
        .asLiveData(viewModelScope.coroutineContext)

    val commandResultSent = SingleLiveEvent<Unit>()

    init {
        viewModelScope.launch {
            try {
                resultsInteractor.sendAttemptResult(launcher.attemptId)
            } catch (e: Exception) {
                e.showLocalizedMessage()
            } finally {
                commandResultSent.call()
            }
        }
    }
}