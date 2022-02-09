package io.flaterlab.meshexam.examination.ui.question

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
internal data class QuestionLauncher(
    val questionId: String,
) : Parcelable