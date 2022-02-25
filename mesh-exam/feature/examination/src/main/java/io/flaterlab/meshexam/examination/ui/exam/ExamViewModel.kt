package io.flaterlab.meshexam.examination.ui.exam

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.flaterlab.meshexam.androidbase.BaseViewModel
import io.flaterlab.meshexam.androidbase.SingleLiveEvent
import io.flaterlab.meshexam.androidbase.getLauncher
import io.flaterlab.meshexam.domain.interactor.ExaminationInteractor
import io.flaterlab.meshexam.examination.dvo.ExaminationDvo
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
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

    val examMeta = MutableLiveData<ExaminationDvo>()
    val timeLeft = MutableLiveData<String>()
    val questionIds = examInteractor
        .questionIdsByExamId(launcher.examId)
        .asLiveData(viewModelScope.coroutineContext)

    val commandShowFinishingConfirm = SingleLiveEvent<Unit>()
    val commandFinishExam = SingleLiveEvent<String>()

    val attemptId get() = launcher.attemptId

    private var timerJob: Job? = null

    init {
        loadAttemptMeta()
    }

    private fun loadAttemptMeta() {
        viewModelScope.launch {
            try {
                val attemptModel = examInteractor.getAttemptById(launcher.attemptId)
                Timber.d("Attempt: $attemptModel")
                examMeta.value = ExaminationDvo(attemptModel.examName, attemptModel.examInfo)
                setTimer(attemptModel.leftTimeInMillis)
            } catch (ex: Exception) {
                ex.showLocalizedMessage()
            }
        }
    }

    private fun setTimer(leftMillis: Long) {
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            var sec = leftMillis / 1000
            while (isActive && sec > 0) {
                delay(1000)
                timeLeft.value =
                    SimpleDateFormat("mm:ss", Locale.getDefault()).format(Date(sec * 1000))
                sec--
            }
        }
    }

    fun onSubmitClicked() {
        commandShowFinishingConfirm.call()
    }

    fun onBackPressed() {
        commandShowFinishingConfirm.call()
    }

    fun onFinishConfirmed() {
        viewModelScope.launch {
            examInteractor.finishAttempt(launcher.examId)
            commandFinishExam.value = launcher.attemptId
        }
    }
}