package io.flaterlab.meshexam.result.ui.send

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class SendResultLauncher(
    val attemptId: String,
) : Parcelable