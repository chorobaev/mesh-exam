package io.flaterlab.meshexam.presentation.profile.edit

import android.os.Bundle
import android.view.View
import androidx.fragment.app.FragmentManager
import dagger.hilt.android.AndroidEntryPoint
import io.flaterlab.meshexam.androidbase.TextWatcherManager
import io.flaterlab.meshexam.androidbase.ViewBindingBottomSheetDialogFragment
import io.flaterlab.meshexam.androidbase.ViewBindingProvider
import io.flaterlab.meshexam.androidbase.bindTextWatcher
import io.flaterlab.meshexam.androidbase.ext.clickWithDebounce
import io.flaterlab.meshexam.androidbase.ext.setOnDoneClickListener
import io.flaterlab.meshexam.androidbase.text.setError
import io.flaterlab.meshexam.presentation.profile.databinding.DialogEditProfileBinding

@AndroidEntryPoint
internal class EditProfileDialogFragment :
    ViewBindingBottomSheetDialogFragment<DialogEditProfileBinding>() {

    companion object {
        fun show(fm: FragmentManager) {
            EditProfileDialogFragment().show(
                fm,
                EditProfileDialogFragment::class.java.canonicalName
            )
        }
    }

    private val viewModel: EditProfileViewModel by vm()
    private val watchManager = TextWatcherManager(this)

    override val isFullScreen: Boolean = true

    override val viewBinder: ViewBindingProvider<DialogEditProfileBinding>
        get() = DialogEditProfileBinding::inflate

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.firstNameError.observe(viewLifecycleOwner, binding.tetFirstName::setError)
        viewModel.lastNameError.observe(viewLifecycleOwner, binding.tetLastName::setError)
        viewModel.isSaveEnabled.observe(viewLifecycleOwner, binding.btnEditProfileSave::setEnabled)
        viewModel.userProfile.observe(viewLifecycleOwner) { dvo ->
            with(binding) {
                if (dvo.firstName.isNotBlank()) tetFirstName.editText.setText(dvo.firstName)
                if (dvo.lastName.isNotBlank()) tetLastName.editText.setText(dvo.lastName)
                if (dvo.info.isNotBlank()) tetInfo.editText.setText(dvo.info)
            }
        }
        viewModel.commandOnSaved.observe(viewLifecycleOwner) { dismiss() }

        binding.tetFirstName.editText.bindTextWatcher(watchManager) {
            viewModel.onFirstNameChanged(it?.toString())
        }
        binding.tetLastName.editText.bindTextWatcher(watchManager) {
            viewModel.onLastNameChanged(it?.toString())
        }
        binding.tetInfo.editText.bindTextWatcher(watchManager) {
            viewModel.onInfoChanged(it?.toString())
        }


        binding.btnEditProfileSave.clickWithDebounce(action = viewModel::onSavePressed)

        binding.tetInfo.editText.setOnDoneClickListener(viewModel::onSavePressed)
    }
}