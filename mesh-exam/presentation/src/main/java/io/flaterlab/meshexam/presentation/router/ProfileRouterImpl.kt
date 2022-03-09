package io.flaterlab.meshexam.presentation.router

import io.flaterlab.meshexam.androidbase.GlobalNavControllerProvider
import io.flaterlab.meshexam.androidbase.toBundleArgs
import io.flaterlab.meshexam.presentation.R
import io.flaterlab.meshexam.presentation.profile.router.ProfileRouter
import io.flaterlab.meshexam.result.ClientResultLauncher
import io.flaterlab.meshexam.result.HostResultLauncher
import io.flaterlab.meshexam.result.ResultLauncher
import javax.inject.Inject

internal class ProfileRouterImpl @Inject constructor(
    private val globalNavControllerProvider: GlobalNavControllerProvider,
) : ProfileRouter {

    override fun openHostedExamResult(attemptId: String) {
        openResults(HostResultLauncher(attemptId))
    }

    override fun openAttemptedExamResult(attemptId: String) {
        openResults(ClientResultLauncher(attemptId))
    }

    private fun openResults(launcher: ResultLauncher) {
        globalNavControllerProvider
            .get()
            .navigate(R.id.action_global_nav_result, launcher.toBundleArgs())
    }
}