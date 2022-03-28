package io.flaterlab.meshexam.examination.ui.result

import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.flaterlab.meshexam.androidbase.BaseViewModel
import io.flaterlab.meshexam.androidbase.SingleLiveEvent
import io.flaterlab.meshexam.androidbase.getLauncher
import io.flaterlab.meshexam.domain.interactor.ExaminationInteractor
import io.flaterlab.meshexam.examination.dvo.AttemptResultDvo
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@HiltViewModel
internal class ResultViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    examInteractor: ExaminationInteractor,
) : BaseViewModel() {

    private val launcher: ResultLauncher = savedStateHandle.getLauncher()

    private val dateFormatter = SimpleDateFormat("mm:ss", Locale.getDefault())
    private val datePrototype = Date()

    val attemptResult: LiveData<AttemptResultDvo> =
        examInteractor.attemptResult(launcher.attemptId)
            .map { model ->
                datePrototype.time = model.durationInMillis
                AttemptResultDvo(
                    examName = model.examName,
                    duration = dateFormatter.format(datePrototype),
                )
            }
            .catch { e -> e.showLocalizedMessage() }
            .asLiveData(viewModelScope.coroutineContext)


    val commandGoToMain = SingleLiveEvent<Unit>()

    fun onGoMainClicked() {
        commandGoToMain.call()
    }
}