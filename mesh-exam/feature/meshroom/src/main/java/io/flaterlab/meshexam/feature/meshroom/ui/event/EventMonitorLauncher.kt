package io.flaterlab.meshexam.feature.meshroom.ui.event

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
internal data class EventMonitorLauncher(
    val hostingId: String
) : Parcelable