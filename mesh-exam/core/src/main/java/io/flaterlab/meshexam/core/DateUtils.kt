package io.flaterlab.meshexam.core

import java.text.SimpleDateFormat
import java.util.*

object DateUtils {

    fun formatTimeMmSs(millis: Long): String {
        return SimpleDateFormat("mm:ss", Locale.getDefault()).format(Date(millis))
    }
}