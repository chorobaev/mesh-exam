package io.flaterlab.meshexam.presentation.profile.main

import androidx.lifecycle.MutableLiveData
import io.flaterlab.meshexam.androidbase.BaseViewModel
import io.flaterlab.meshexam.androidbase.SingleLiveEvent
import io.flaterlab.meshexam.presentation.profile.dvo.HistoryDvo
import io.flaterlab.meshexam.presentation.profile.dvo.UserProfileDvo
import io.flaterlab.meshexam.uikit.view.StateRecyclerView
import javax.inject.Inject

internal class ProfileViewModel @Inject constructor(

) : BaseViewModel() {

    val userProfile = MutableLiveData<UserProfileDvo>()
    val historyItems = MutableLiveData<List<HistoryDvo>>(emptyList())
    val historyListState = MutableLiveData(StateRecyclerView.State.NORMAL)

    val commandEditName = SingleLiveEvent<Unit>()

    init {
        historyItems.value = (1..10).map {
            HistoryDvo(it.toString(), "History $it", 50, it % 3 == 0)
        }
    }

    fun onEditProfileClicked() {
        commandEditName.call()
    }

    fun onHistoryItemClicked(item: HistoryDvo) {

    }
}