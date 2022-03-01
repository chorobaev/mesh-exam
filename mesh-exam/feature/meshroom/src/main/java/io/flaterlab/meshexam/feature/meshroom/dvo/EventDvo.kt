package io.flaterlab.meshexam.feature.meshroom.dvo

internal data class EventDvo(
    val title: String,
    val owner: String,
    val timeInMillis: Long,
    val isActive: Boolean
)