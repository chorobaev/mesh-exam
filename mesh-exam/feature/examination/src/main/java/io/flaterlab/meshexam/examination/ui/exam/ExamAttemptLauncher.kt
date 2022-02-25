package io.flaterlab.meshexam.examination.ui.exam

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
internal data class ExamAttemptLauncher(
    val examId: String,
    val attemptId: String,
) : Parcelable