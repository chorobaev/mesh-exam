package io.flaterlab.meshexam.presentation.router

import io.flaterlab.meshexam.androidbase.GlobalNavControllerProvider
import io.flaterlab.meshexam.androidbase.toBundleArgs
import io.flaterlab.meshexam.feature.meshroom.router.MeshroomRouter
import io.flaterlab.meshexam.presentation.R
import io.flaterlab.meshexam.result.HostResultLauncher
import javax.inject.Inject

internal class MeshroomRouterImpl @Inject constructor(
    private val generalNavControllerProvider: GlobalNavControllerProvider,
) : MeshroomRouter {

    override fun openResultsScreen(attemptId: String) {
        generalNavControllerProvider.get().navigate(
            R.id.action_global_nav_result,
            HostResultLauncher(attemptId).toBundleArgs()
        )
    }
}