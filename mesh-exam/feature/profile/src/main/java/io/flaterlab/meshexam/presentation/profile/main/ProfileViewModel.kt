package io.flaterlab.meshexam.presentation.profile.main

import androidx.lifecycle.MutableLiveData
import io.flaterlab.meshexam.androidbase.BaseViewModel
import io.flaterlab.meshexam.androidbase.SingleLiveEvent
import io.flaterlab.meshexam.presentation.profile.dvo.HistoryDvo
import io.flaterlab.meshexam.presentation.profile.dvo.UserProfileDvo
import javax.inject.Inject

internal class ProfileViewModel @Inject constructor(

) : BaseViewModel() {

    val userProfile = MutableLiveData<UserProfileDvo>()
    val historyItems = MutableLiveData<List<HistoryDvo>>(emptyList())

    val commandEditName = SingleLiveEvent<Unit>()

    fun onEditProfileClicked() {
        commandEditName.call()
    }

    fun onHistoryItemClicked(item: HistoryDvo) {

    }
}