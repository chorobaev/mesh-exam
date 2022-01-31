package io.flaterlab.meshexam.create.ui.question

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CreateQuestionLauncher(
    val examId: String,
    val actionBehavior: CreateQuestionActionBehavior? = null,
) : Parcelable