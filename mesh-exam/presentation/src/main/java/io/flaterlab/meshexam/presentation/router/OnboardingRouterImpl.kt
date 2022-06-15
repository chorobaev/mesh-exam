package io.flaterlab.meshexam.presentation.router

import io.flaterlab.meshexam.androidbase.text.Text
import io.flaterlab.meshexam.onboarding.ui.OnboardingRouter
import io.flaterlab.meshexam.presentation.profile.navigator.ProfileNavigator
import javax.inject.Inject

internal class OnboardingRouterImpl @Inject constructor(
    private val profileNavigator: ProfileNavigator,
) : OnboardingRouter {

    override fun openEditProfile(title: Text) = profileNavigator.openEditProfile(title)
}