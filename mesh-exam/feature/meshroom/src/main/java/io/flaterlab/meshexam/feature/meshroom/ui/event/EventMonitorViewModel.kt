package io.flaterlab.meshexam.feature.meshroom.ui.event

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import dagger.hilt.android.lifecycle.HiltViewModel
import io.flaterlab.meshexam.androidbase.BaseViewModel
import io.flaterlab.meshexam.androidbase.getLauncher
import io.flaterlab.meshexam.core.Mapper
import io.flaterlab.meshexam.domain.exam.model.ExamEventModel
import io.flaterlab.meshexam.domain.interactor.MeshInteractor
import io.flaterlab.meshexam.feature.meshroom.dvo.EventDvo
import io.flaterlab.meshexam.uikit.view.StateRecyclerView
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
internal class EventMonitorViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    meshInteractor: MeshInteractor,
    private val eventMapper: Mapper<ExamEventModel, EventDvo>,
) : BaseViewModel() {

    private val launcher: EventMonitorLauncher = savedStateHandle.getLauncher()

    val eventList = meshInteractor.examEventList(launcher.hostingId)
        .map { list ->
            list.map(eventMapper::invoke)
        }
        .onEach { list ->
            eventListState.value = if (list.isEmpty()) {
                StateRecyclerView.State.EMPTY
            } else {
                StateRecyclerView.State.NORMAL
            }
        }
        .catch { e -> e.showLocalizedMessage() }
    val eventListState = MutableLiveData(StateRecyclerView.State.NORMAL)
}