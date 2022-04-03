package io.flaterlab.meshexam.feature.meshroom.dvo

import io.flaterlab.meshexam.androidbase.text.Text

internal data class EventDvo(
    val title: Text,
    val owner: String,
    val timeInMillis: Long,
    val isActive: Boolean
)