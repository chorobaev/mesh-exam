package io.flaterlab.meshexam.examination

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class ExamLauncher(
    val examId: String
) : Parcelable