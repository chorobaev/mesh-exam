package io.flaterlab.meshexam.presentation.router

import io.flaterlab.meshexam.androidbase.GlobalNavControllerProvider
import io.flaterlab.meshexam.androidbase.text.Text
import io.flaterlab.meshexam.androidbase.toBundleArgs
import io.flaterlab.meshexam.examination.ExamLauncher
import io.flaterlab.meshexam.presentation.R
import io.flaterlab.meshexam.presentation.discover.router.DiscoverRouter
import io.flaterlab.meshexam.presentation.profile.navigator.ProfileNavigator
import javax.inject.Inject

internal class DiscoverRouterImpl @Inject constructor(
    private val profileNavigator: ProfileNavigator,
    private val generalNavControllerProvider: GlobalNavControllerProvider,
) : DiscoverRouter {

    override fun openEditProfile(title: Text) = profileNavigator.openEditProfile(title)

    override fun joinExam(examId: String) {
        generalNavControllerProvider.get()
            .navigate(
                R.id.action_global_nav_examination,
                ExamLauncher(examId).toBundleArgs()
            )
    }
}