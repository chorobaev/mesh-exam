package io.flaterlab.meshexam.feature.meshroom.ui.result

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
internal data class HostResultLauncher(
    val attemptId: String,
) : Parcelable