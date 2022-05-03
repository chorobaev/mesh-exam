package io.flaterlab.meshexam.presentation.router

import io.flaterlab.meshexam.androidbase.GlobalNavControllerProvider
import io.flaterlab.meshexam.androidbase.toBundleArgs
import io.flaterlab.meshexam.create.router.CreateExamRouter
import io.flaterlab.meshexam.create.ui.question.CreateQuestionLauncher
import io.flaterlab.meshexam.presentation.R
import javax.inject.Inject

internal class CreateExamRouterImpl @Inject constructor(
    private val globalNavControllerProvider: GlobalNavControllerProvider,
) : CreateExamRouter {

    override fun openCreateQuestions(examId: String) {
        globalNavControllerProvider.get().navigate(
            R.id.action_global_nav_create_question,
            CreateQuestionLauncher(examId).toBundleArgs()
        )
    }
}