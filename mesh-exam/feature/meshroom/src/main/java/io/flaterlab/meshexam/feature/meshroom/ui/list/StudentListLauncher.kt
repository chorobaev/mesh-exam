package io.flaterlab.meshexam.feature.meshroom.ui.list

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class StudentListLauncher(
    val examId: String,
    val hostingId: String,
) : Parcelable