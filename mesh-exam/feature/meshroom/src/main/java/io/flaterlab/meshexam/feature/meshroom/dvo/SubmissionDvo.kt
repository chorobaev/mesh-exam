package io.flaterlab.meshexam.feature.meshroom.dvo

import android.content.Context
import androidx.annotation.ColorRes
import io.flaterlab.meshexam.androidbase.common.adapter.ClientListAdapter
import io.flaterlab.meshexam.androidbase.text.Text
import io.flaterlab.meshexam.uikit.ext.getColorCompat

internal data class SubmissionDvo(
    override val id: String,
    override val fullName: String,
    override val info: Text,
    override val status: Text,
    @ColorRes val statusColorResId: Int,
) : ClientListAdapter.ClientItem {

    override fun provideStatusTextColor(context: Context): Int {
        return context.getColorCompat(statusColorResId)
    }
}