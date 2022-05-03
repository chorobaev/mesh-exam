package io.flaterlab.meshexam.presentation.router

import io.flaterlab.meshexam.androidbase.GlobalNavControllerProvider
import io.flaterlab.meshexam.androidbase.toBundleArgs
import io.flaterlab.meshexam.create.router.CreateQuestionRouter
import io.flaterlab.meshexam.create.ui.exam.CreateExamLauncher
import io.flaterlab.meshexam.presentation.R
import javax.inject.Inject

internal class CreateQuestionRouterImpl @Inject constructor(
    private val generalNavControllerProvider: GlobalNavControllerProvider,
) : CreateQuestionRouter {

    override fun openEditExamGeneralInfo(examId: String) {
        generalNavControllerProvider.get().navigate(
            R.id.action_global_nav_create_exam,
            CreateExamLauncher(examId).toBundleArgs()
        )
    }
}