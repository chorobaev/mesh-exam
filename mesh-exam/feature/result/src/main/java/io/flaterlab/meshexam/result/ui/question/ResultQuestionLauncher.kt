package io.flaterlab.meshexam.result.ui.question

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
internal class ResultQuestionLauncher(
    val questionId: String,
    val attemptId: String,
): Parcelable