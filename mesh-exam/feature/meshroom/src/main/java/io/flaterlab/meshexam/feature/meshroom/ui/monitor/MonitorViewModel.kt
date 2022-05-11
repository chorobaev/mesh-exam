package io.flaterlab.meshexam.feature.meshroom.ui.monitor

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.flaterlab.meshexam.androidbase.BaseViewModel
import io.flaterlab.meshexam.androidbase.SingleLiveEvent
import io.flaterlab.meshexam.androidbase.getLauncher
import io.flaterlab.meshexam.domain.create.usecase.GetExamUseCase
import io.flaterlab.meshexam.domain.interactor.MeshInteractor
import io.flaterlab.meshexam.domain.mesh.model.HostingMetaModel
import io.flaterlab.meshexam.feature.meshroom.dvo.ExamInfoDvo
import io.flaterlab.meshexam.feature.meshroom.ui.finishing.FinishingLauncher
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@HiltViewModel
internal class MonitorViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    getExamUseCase: GetExamUseCase,
    private val meshInteractor: MeshInteractor,
) : BaseViewModel() {

    val launcher: MonitorLauncher = savedStateHandle.getLauncher()
    private val dateFormatter = SimpleDateFormat("mm:ss", Locale.getDefault())
    private val datePrototype = Date()

    val exam = getExamUseCase(launcher.examId)
        .map { it.exam }
        .map { info ->
            ExamInfoDvo(info.id, info.name, info.type)
        }
        .asLiveData(viewModelScope.coroutineContext)
    val timer = meshInteractor.hostingTimeLeftInSec(launcher.hostingId)
        .map { sec ->
            datePrototype.time = sec * 1000L
            dateFormatter.format(datePrototype)
        }
        .asLiveData(viewModelScope.coroutineContext)

    val commandShowFinishPrompt = SingleLiveEvent<Unit>()
    val commandFinishExam = SingleLiveEvent<FinishingLauncher>()

    init {
        meshInteractor.hostingState(launcher.hostingId)
            .onEach { hostingMeta ->
                if (hostingMeta.status == HostingMetaModel.HostingStatus.FINISHED) {
                    commandFinishExam.value = FinishingLauncher(launcher.hostingId)
                }
            }
            .catch { it.showLocalizedMessage() }
            .launchIn(viewModelScope)
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
        }
    }
}