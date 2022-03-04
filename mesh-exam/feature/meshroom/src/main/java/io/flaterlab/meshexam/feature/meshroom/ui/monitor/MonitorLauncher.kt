package io.flaterlab.meshexam.feature.meshroom.ui.monitor

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class MonitorLauncher(
    val examId: String,
    val attemptId: String,
) : Parcelable