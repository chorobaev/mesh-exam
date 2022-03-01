package io.flaterlab.meshexam.feature.meshroom.ui.event

import androidx.lifecycle.MutableLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import io.flaterlab.meshexam.androidbase.BaseViewModel
import io.flaterlab.meshexam.feature.meshroom.dvo.EventDvo
import io.flaterlab.meshexam.uikit.view.StateRecyclerView
import kotlinx.coroutines.flow.MutableStateFlow
import java.util.*
import javax.inject.Inject

@HiltViewModel
internal class EventMonitorViewModel @Inject constructor(

) : BaseViewModel() {

    val eventList = MutableStateFlow<List<EventDvo>>(emptyList())
    val eventListState = MutableLiveData(StateRecyclerView.State.NORMAL)

    init {
        // TODO: add actual implementation
        eventList.value = (1..10).map {
            EventDvo(
                title = "Event #$it",
                owner = listOf("Jan Van", "Nurbol", "Someone").random(),
                timeInMillis = Date().time,
                isActive = listOf(true, false).random(),
            )
        }
    }
}