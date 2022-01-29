package io.flaterlab.meshexam.presentation.exams.dvo

import io.flaterlab.meshexam.androidbase.common.adapter.ExamListAdapter

internal data class ExamDvo(
    val id: String,
    override val name: String,
    override val durationInMin: Long
) : ExamListAdapter.ExamItem