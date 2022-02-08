package io.flaterlab.meshexam.presentation.profile.navigator

import androidx.fragment.app.Fragment
import io.flaterlab.meshexam.presentation.profile.ui.edit.EditProfileDialogFragment
import javax.inject.Inject

class ProfileNavigator @Inject constructor(
    private val fragment: Fragment
) {

    fun openEditProfile() {
        EditProfileDialogFragment.show(fragment.childFragmentManager)
    }
}