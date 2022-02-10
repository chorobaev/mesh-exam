package io.flaterlab.meshexam.presentation.discover.router

import io.flaterlab.meshexam.androidbase.text.Text

interface DiscoverRouter {

    fun openEditProfile(title: Text)

    fun joinExam(examId: String)
}