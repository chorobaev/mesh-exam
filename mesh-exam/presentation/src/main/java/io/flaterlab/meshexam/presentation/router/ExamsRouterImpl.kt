package io.flaterlab.meshexam.presentation.router

import io.flaterlab.meshexam.androidbase.GlobalNavControllerProvider
import io.flaterlab.meshexam.presentation.R
import io.flaterlab.meshexam.presentation.exams.router.ExamsRouter
import javax.inject.Inject

internal class ExamsRouterImpl @Inject constructor(
    private val navControllerProvider: GlobalNavControllerProvider,
) : ExamsRouter {

    override fun openCreateExam() {
        navControllerProvider.get().navigate(R.id.action_homeFragment_to_nav_create_exam)
    }
}