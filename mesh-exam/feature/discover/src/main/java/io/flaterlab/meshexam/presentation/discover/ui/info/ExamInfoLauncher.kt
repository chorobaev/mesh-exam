package io.flaterlab.meshexam.presentation.discover.ui.info

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
internal data class ExamInfoLauncher(
    val examId: String,
    val examName: String,
    val examDurationInMin: Int,
    val examHostName: String,
) : Parcelable