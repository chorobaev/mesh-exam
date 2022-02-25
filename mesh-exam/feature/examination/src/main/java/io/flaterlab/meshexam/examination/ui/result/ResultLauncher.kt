package io.flaterlab.meshexam.examination.ui.result

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
internal data class ResultLauncher(
    val attemptId: String
) : Parcelable