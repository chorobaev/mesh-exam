package io.flaterlab.meshexam.onboarding.ui

import dagger.hilt.android.lifecycle.HiltViewModel
import io.flaterlab.meshexam.androidbase.BaseViewModel
import io.flaterlab.meshexam.domain.interactor.ProfileInteractor
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
internal class OnboardingViewModel @Inject constructor(
    profileInteractor: ProfileInteractor,
) : BaseViewModel() {

    val userProfileSetUp = profileInteractor.userProfile()
        .map { it.fullName.isNotBlank() }
}