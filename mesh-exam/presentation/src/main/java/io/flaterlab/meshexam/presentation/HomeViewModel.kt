package io.flaterlab.meshexam.presentation

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.flaterlab.meshexam.androidbase.BaseViewModel
import io.flaterlab.meshexam.androidbase.SingleLiveEvent
import io.flaterlab.meshexam.domain.interactor.ProfileInteractor
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class HomeViewModel @Inject constructor(
    private val profileInteractor: ProfileInteractor,
) : BaseViewModel() {

    val showOnboarding = SingleLiveEvent<Unit>()

    init {
        viewModelScope.launch {
            if (profileInteractor.isFirstAppStartup()) {
                showOnboarding.call()
            }
        }
    }
}