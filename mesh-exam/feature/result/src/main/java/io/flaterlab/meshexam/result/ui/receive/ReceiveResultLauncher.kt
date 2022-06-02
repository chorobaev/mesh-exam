package io.flaterlab.meshexam.result.ui.receive

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ReceiveResultLauncher(
    val hostingId: String,
) : Parcelable