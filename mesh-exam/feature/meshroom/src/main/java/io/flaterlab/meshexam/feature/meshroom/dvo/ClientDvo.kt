package io.flaterlab.meshexam.feature.meshroom.dvo

import android.content.Context
import io.flaterlab.meshexam.androidbase.common.adapter.ClientListAdapter
import io.flaterlab.meshexam.feature.meshroom.R
import io.flaterlab.meshexam.uikit.ext.getColorAttr

data class ClientDvo(
    override val id: String,
    override val fullName: String,
    override val info: String,
    override val status: String
) : ClientListAdapter.ClientItem {

    override fun provideStatusTextColor(context: Context): Int {
        return context.getColorAttr(R.attr.colorOnBackgroundLight)
    }
}