package io.flaterlab.meshexam.create.ui.question

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
internal data class CreateQuestionLauncher(
    val examId: String
): Parcelable