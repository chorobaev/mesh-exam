package io.flaterlab.meshexam.presentation.discover.dvo

import io.flaterlab.meshexam.androidbase.common.adapter.ExamListAdapter

data class AvailableExamDvo(
    override val id: String,
    override val name: String,
    val hostName: String,
    override val durationInMin: Long,
) : ExamListAdapter.ExamItem