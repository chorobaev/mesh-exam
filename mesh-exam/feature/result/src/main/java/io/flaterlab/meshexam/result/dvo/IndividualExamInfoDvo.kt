package io.flaterlab.meshexam.result.dvo

import io.flaterlab.meshexam.androidbase.text.Text

internal data class IndividualExamInfoDvo(
    val id: String,
    val name: String,
    val info: String,
    val generalInfoList: List<Pair<Text, Text>>,
)