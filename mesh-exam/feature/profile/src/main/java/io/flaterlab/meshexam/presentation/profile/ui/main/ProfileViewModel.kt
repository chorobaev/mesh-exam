package io.flaterlab.meshexam.presentation.profile.ui.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import io.flaterlab.meshexam.androidbase.BaseViewModel
import io.flaterlab.meshexam.androidbase.SingleLiveEvent
import io.flaterlab.meshexam.domain.profile.usecase.GetUserProfileUseCase
import io.flaterlab.meshexam.presentation.profile.dvo.HistoryDvo
import io.flaterlab.meshexam.presentation.profile.dvo.UserProfileDvo
import io.flaterlab.meshexam.uikit.view.StateRecyclerView
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
internal class ProfileViewModel @Inject constructor(
    getUserProfileUseCase: GetUserProfileUseCase,
) : BaseViewModel() {

    val userProfile = getUserProfileUseCase()
        .map { model ->
            UserProfileDvo(model.firstName, model.lastName, model.initials, model.info)
        }
        .catch {
            Timber.e(it)
            it.showLocalizedMessage()
        }
        .asLiveData()
    val historyItems = MutableLiveData<List<HistoryDvo>>()
    val historyListState = MutableLiveData(StateRecyclerView.State.NORMAL)

    val commandEditName = SingleLiveEvent<Unit>()
    val commandOpenAttemptedResults = SingleLiveEvent<String>()
    val commandOpenHostedResults = SingleLiveEvent<String>()

    init {
        // TODO: implement with actual data
        historyItems.value = (1..10).map {
            HistoryDvo(it.toString(), "Exam #$it", 30, listOf(true, false).random())
        }
    }

    fun onEditProfileClicked() {
        commandEditName.call()
    }

    fun onHistoryItemClicked(item: HistoryDvo) {
        if (item.isHosted) {
            commandOpenHostedResults.value = item.id
        } else {
            commandOpenAttemptedResults.value = item.id
        }
    }
}