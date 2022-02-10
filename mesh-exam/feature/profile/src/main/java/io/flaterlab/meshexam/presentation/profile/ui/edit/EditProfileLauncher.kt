package io.flaterlab.meshexam.presentation.profile.ui.edit

import android.os.Parcelable
import io.flaterlab.meshexam.androidbase.text.Text
import kotlinx.parcelize.Parcelize

@Parcelize
data class EditProfileLauncher(
    val title: Text,
) : Parcelable