package io.flaterlab.meshexam.presentation.profile.edit

import androidx.lifecycle.MutableLiveData
import io.flaterlab.meshexam.androidbase.BaseViewModel
import io.flaterlab.meshexam.androidbase.SingleLiveEvent
import io.flaterlab.meshexam.androidbase.text.Text
import io.flaterlab.meshexam.presentation.profile.R
import io.flaterlab.meshexam.presentation.profile.dvo.UserProfileDvo
import javax.inject.Inject

internal class EditProfileViewModel @Inject constructor(

) : BaseViewModel() {

    val userProfile = MutableLiveData<UserProfileDvo>()
    val isSaveEnabled = MutableLiveData(false)

    val firstNameError = MutableLiveData(Text.empty())
    val lastNameError = MutableLiveData(Text.empty())

    val commandOnSaved = SingleLiveEvent<Unit>()

    private var firstName: String? = null
    private var lastName: String? = null
    private var info: String? = null

    fun onFirstNameChanged(name: String?) {
        firstName = name
        firstNameError.value = if (isValidFirstName()) {
            Text.empty()
        } else {
            Text.from(R.string.profile_edit_firstNameErrorMessage)
        }
        updateSaveButtonAvailability()
    }

    private fun isValidFirstName(): Boolean {
        return firstName != null && firstName?.length ?: 0 >= NAME_MIN_LENGTH
    }

    private fun updateSaveButtonAvailability() {
        isSaveEnabled.value = isSaveButtonAvailable()
    }

    private fun isSaveButtonAvailable(): Boolean {
        return isValidFirstName() && isValidLastName() &&
                (firstName != userProfile.value?.firstName ||
                        lastName != userProfile.value?.lastName)
    }

    fun onLastNameChanged(name: String?) {
        lastName = name
        lastNameError.value = if (isValidLastName()) {
            Text.empty()
        } else {
            Text.from(R.string.profile_edit_lastNameErrorMessage)
        }
        updateSaveButtonAvailability()
    }

    private fun isValidLastName(): Boolean {
        return lastName != null && lastName?.length ?: 0 >= NAME_MIN_LENGTH
    }

    fun onInfoChanged(info: String?) {
        this.info = info
    }

    fun onSavePressed() {
        if (isSaveButtonAvailable()) {

        }
    }

    companion object {
        private const val NAME_MIN_LENGTH = 2
    }
}