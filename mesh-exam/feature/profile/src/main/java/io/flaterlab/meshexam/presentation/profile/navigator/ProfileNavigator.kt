package io.flaterlab.meshexam.presentation.profile.navigator

import androidx.fragment.app.Fragment
import io.flaterlab.meshexam.androidbase.text.Text
import io.flaterlab.meshexam.presentation.profile.ui.edit.EditProfileDialogFragment
import io.flaterlab.meshexam.presentation.profile.ui.edit.EditProfileLauncher
import javax.inject.Inject

class ProfileNavigator @Inject constructor(
    private val fragment: Fragment
) {

    fun openEditProfile(title: Text) {
        EditProfileDialogFragment.show(
            EditProfileLauncher(title),
            fragment.childFragmentManager
        )
    }
}