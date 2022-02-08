package io.flaterlab.meshexam.presentation.router

import io.flaterlab.meshexam.presentation.discover.router.DiscoverRouter
import io.flaterlab.meshexam.presentation.profile.navigator.ProfileNavigator
import javax.inject.Inject

internal class DiscoverRouterImpl @Inject constructor(
    private val profileNavigator: ProfileNavigator,
) : DiscoverRouter {

    override fun openEditProfile() = profileNavigator.openEditProfile()

    override fun joinExam(examId: String) {
        TODO("Not implemented yet")
    }
}