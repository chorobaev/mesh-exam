package io.flaterlab.meshexam.create.ui.question

import android.os.Parcelable
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment

interface CreateQuestionActionBehavior : Parcelable {

    @get:StringRes
    val actionTitleResId: Int

    fun onActionClicked(fragment: Fragment)
}