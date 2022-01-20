package io.flaterlab.feature.main.message

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class MessageDvo(
    val text: String,
): Parcelable