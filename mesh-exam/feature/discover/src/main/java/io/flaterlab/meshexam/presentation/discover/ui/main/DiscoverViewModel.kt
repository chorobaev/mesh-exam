package io.flaterlab.meshexam.presentation.discover.ui.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.flaterlab.meshexam.androidbase.BaseViewModel
import io.flaterlab.meshexam.androidbase.SingleLiveEvent
import io.flaterlab.meshexam.domain.interactor.ExaminationInteractor
import io.flaterlab.meshexam.presentation.discover.dvo.AvailableExamDvo
import io.flaterlab.meshexam.uikit.view.StateRecyclerView
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds

@HiltViewModel
class DiscoverViewModel @Inject constructor(
    private val examinationInteractor: ExaminationInteractor,
) : BaseViewModel() {

    val exams = MutableLiveData<List<AvailableExamDvo>>()
    val examListState = exams.map { list ->
        when {
            list.isEmpty() -> StateRecyclerView.State.EMPTY
            else -> StateRecyclerView.State.NORMAL
        }
    }
    val discovering = MutableLiveData(false)
    val permissionNeededState = MutableLiveData(false)

    val commandOpenExam = SingleLiveEvent<AvailableExamDvo>()
    val commandRequestPermission = SingleLiveEvent<Unit>()
    val commandObserveExams = SingleLiveEvent<Unit>()

    private var discoverJob: Job? = null

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun discoverExams() {
        discoverJob?.cancel()
        exams.value = emptyList()
        discovering.value = true
        discoverJob = flow<Unit> {
            emit(Unit)
            delay(DISCOVERY_DURATION)
            currentCoroutineContext().cancel()
        }.flatMapLatest {
            examinationInteractor.discoverExams()
                .map { list ->
                    list.map { exam ->
                        AvailableExamDvo(exam.id, exam.name, exam.host, exam.duration)
                    }
                }
                .onEach(exams::setValue)
                .catch { it.showLocalizedMessage() }
        }
            .onCompletion {
                discovering.value = false
            }
            .launchIn(viewModelScope)
    }

    fun onRefreshPressed() {
        discoverExams()
    }

    fun onScreenShown() {
        discoverExams()
    }

    fun onScreenHidden() {
        discoverJob?.cancel()
        discoverJob = null
        exams.value = emptyList()
    }

    fun onPermissionsChanged(granted: Boolean, shouldRequest: Boolean = false) {
        if (granted) {
            commandObserveExams.call()
        } else if (shouldRequest) {
            commandRequestPermission.call()
        }
        permissionNeededState.value = !granted
    }

    fun onRequestPermissionsClicked() {
        commandRequestPermission.call()
    }

    fun onExamClicked(dvo: AvailableExamDvo) {
        commandOpenExam.value = dvo
    }

    companion object {
        private val DISCOVERY_DURATION = 15.seconds
    }
}