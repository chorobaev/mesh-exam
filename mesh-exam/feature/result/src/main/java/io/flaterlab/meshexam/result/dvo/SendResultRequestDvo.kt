package io.flaterlab.meshexam.result.dvo

import io.flaterlab.meshexam.androidbase.text.Text

data class SendResultRequestDvo(
    val requestUid: String,
    val message: Text,
)