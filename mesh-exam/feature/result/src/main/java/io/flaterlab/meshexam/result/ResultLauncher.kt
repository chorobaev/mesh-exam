package io.flaterlab.meshexam.result

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

sealed class ResultLauncher(val attemptId: String) : Parcelable

@Parcelize
data class HostResultLauncher(
    private val _attemptId: String
) : ResultLauncher(_attemptId)

@Parcelize
data class ClientResultLauncher(
    private val _attemptId: String
) : ResultLauncher(_attemptId)
