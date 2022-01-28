package io.flaterlab.meshexam.create.ui.change

import android.os.Bundle
import android.os.Parcelable
import androidx.annotation.StringRes
import androidx.core.os.bundleOf
import kotlinx.parcelize.Parcelize

@Parcelize
internal data class ChangeTextLauncher(
    val requestKey: String,
    @StringRes val titleResId: Int,
    val text: String,
    val args: Bundle = bundleOf()
) : Parcelable