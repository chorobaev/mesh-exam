package io.flaterlab.meshexam.create.ui.exam

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CreateExamLauncher(
    val examId: String? = null,
) : Parcelable