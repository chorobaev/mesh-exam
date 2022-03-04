package io.flaterlab.meshexam.result.dvo

import android.content.Context
import io.flaterlab.meshexam.androidbase.common.adapter.ClientListAdapter
import io.flaterlab.meshexam.androidbase.text.Text
import io.flaterlab.meshexam.result.R
import io.flaterlab.meshexam.uikit.ext.getColorAttr

internal data class ResultItemDvo(
    override val id: String,
    override val fullName: String,
    override val info: Text,
    override val status: Text,
    private val grade: Int,
    private val totalGrade: Int,
) : ClientListAdapter.ClientItem {

    override val statusInfoProvider: (Context) -> String
        get() = { context -> context.getString(R.string.result_list_item_grade, grade, totalGrade) }

    override fun provideStatusTextColor(context: Context): Int {
        return context.getColorAttr(R.attr.colorOnBackgroundLight)
    }
}