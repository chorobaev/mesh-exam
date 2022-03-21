package io.flaterlab.meshexam.presentation.profile.dvo

internal data class HistoryDvo(
    val id: String,
    val name: String,
    val durationInMin: Int,
    val isHosted: Boolean
)