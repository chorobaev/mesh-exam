package io.flaterlab.meshexam.presentation.profile.router

interface ProfileRouter {

    fun openHostedExamResult(attemptId: String)

    fun openAttemptedExamResult(attemptId: String)
}