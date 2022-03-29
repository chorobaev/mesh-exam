package io.flaterlab.meshexam.feature.meshroom.ui.monitor

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.flaterlab.meshexam.androidbase.BaseViewModel
import io.flaterlab.meshexam.androidbase.SingleLiveEvent
import io.flaterlab.meshexam.androidbase.getLauncher
import io.flaterlab.meshexam.domain.create.usecase.GetExamUseCase
import io.flaterlab.meshexam.domain.interactor.MeshInteractor
import io.flaterlab.meshexam.feature.meshroom.dvo.ExamInfoDvo
import io.flaterlab.meshexam.feature.meshroom.ui.finishing.FinishingLauncher
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@HiltViewModel
internal class MonitorViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getExamUseCase: GetExamUseCase,
    private val meshInteractor: MeshInteractor,
) : BaseViewModel() {

    private val launcher: MonitorLauncher = savedStateHandle.getLauncher()
    private val dateFormatter = SimpleDateFormat("mm:ss", Locale.getDefault())
    private val datePrototype = Date()

    val exam = MutableLiveData<ExamInfoDvo>()
    val timer = meshInteractor.hostingTimeLeftInSec(launcher.hostingId)
        .map { sec ->
            datePrototype.time = sec * 1000L
            dateFormatter.format(datePrototype)
        }
        .asLiveData(viewModelScope.coroutineContext)

    val commandShowFinishPrompt = SingleLiveEvent<Unit>()
    val commandFinishExam = SingleLiveEvent<FinishingLauncher>()

    init {
        loadExam()
    }

    private fun loadExam() {
        viewModelScope.launch {
            val info = getExamUseCase(launcher.examId).exam
            exam.value = ExamInfoDvo(info.id, info.name, info.type)
        }
    }

    fun onBackPressed() {
        commandShowFinishPrompt.call()
    }

    fun onFinishClicked() {
        commandShowFinishPrompt.call()
    }

    fun onFinishConfirmed() {
        viewModelScope.launch {
            meshInteractor.finishExam(launcher.hostingId)
            commandFinishExam.value = FinishingLauncher(launcher.hostingId)
        }
    }
}