package io.flaterlab.meshexam.result.dvo

import io.flaterlab.meshexam.androidbase.text.Text

internal data class IndividualExamInfoDvo(
    val examId: String,
    val name: String,
    val info: String,
    val generalInfoList: List<Pair<Text, Text>>,
    val questionInfoList: List<ResultQuestionInfoDvo>,
)