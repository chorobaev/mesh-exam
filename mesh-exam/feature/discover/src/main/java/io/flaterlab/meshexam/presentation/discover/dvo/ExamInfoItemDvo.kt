package io.flaterlab.meshexam.presentation.discover.dvo

import io.flaterlab.meshexam.androidbase.text.Text

internal data class ExamInfoItemDvo(
    val title: Text,
    val value: Text,
    val isEditable: Boolean = false,
    val onEdit: () -> Unit = {},
)