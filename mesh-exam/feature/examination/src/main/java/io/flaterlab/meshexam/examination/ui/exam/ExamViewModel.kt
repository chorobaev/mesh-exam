package io.flaterlab.meshexam.examination.ui.exam

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.flaterlab.meshexam.androidbase.BaseViewModel
import io.flaterlab.meshexam.androidbase.SingleLiveEvent
import io.flaterlab.meshexam.androidbase.getLauncher
import io.flaterlab.meshexam.domain.exam.model.AttemptMetaModel
import io.flaterlab.meshexam.domain.interactor.ExaminationInteractor
import io.flaterlab.meshexam.examination.dvo.ExaminationDvo
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@HiltViewModel
internal class ExamViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val examInteractor: ExaminationInteractor,
) : BaseViewModel() {

    private val launcher: ExamAttemptLauncher = savedStateHandle.getLauncher()
    private val dateFormatter = SimpleDateFormat("mm:ss", Locale.getDefault())
    private val datePrototype = Date()

    val examMeta = examInteractor.attemptMetaById(launcher.attemptId)
        .onEach { attemptModel ->
            Timber.d("Attempt meta changed: $attemptModel")
            if (attemptModel.examStatus == AttemptMetaModel.ExamStatus.FINISHED) {
                commandFinishExam.value = launcher.attemptId
            }
        }
        .map { attemptModel ->
            ExaminationDvo(attemptModel.examName, attemptModel.examInfo)
        }
        .catch { e -> e.showLocalizedMessage() }
        .asLiveData(viewModelScope.coroutineContext)

    val timeLeft = examInteractor.attemptTimeLeftInSec(launcher.attemptId)
        .map { sec ->
            datePrototype.time = sec * 1000L
            dateFormatter.format(datePrototype)
        }
        .asLiveData(viewModelScope.coroutineContext)

    val questionIds = examInteractor
        .questionIdsByExamId(launcher.examId)
        .asLiveData(viewModelScope.coroutineContext)

    val commandShowFinishingConfirm = SingleLiveEvent<Unit>()
    val commandFinishExam = SingleLiveEvent<String>()

    val attemptId get() = launcher.attemptId

    fun onSubmitClicked() {
        commandShowFinishingConfirm.call()
    }

    fun onBackPressed() {
        commandShowFinishingConfirm.call()
    }

    fun onFinishConfirmed() {
        viewModelScope.launch {
            try {
                examInteractor.finishAttempt(launcher.attemptId)
            } catch (ex: Exception) {
                Timber.e(ex)
                // TODO: handle mesh destroyed case
            }
            commandFinishExam.value = launcher.attemptId
        }
    }
}