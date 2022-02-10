package io.flaterlab.meshexam.presentation.profile.ui.edit

import android.os.Bundle
import android.view.View
import androidx.fragment.app.FragmentManager
import dagger.hilt.android.AndroidEntryPoint
import io.flaterlab.meshexam.androidbase.*
import io.flaterlab.meshexam.androidbase.ext.clickWithDebounce
import io.flaterlab.meshexam.androidbase.ext.setOnDoneClickListener
import io.flaterlab.meshexam.androidbase.text.Text
import io.flaterlab.meshexam.androidbase.text.setError
import io.flaterlab.meshexam.androidbase.text.setText
import io.flaterlab.meshexam.presentation.profile.R
import io.flaterlab.meshexam.presentation.profile.databinding.DialogEditProfileBinding

@AndroidEntryPoint
internal class EditProfileDialogFragment :
    ViewBindingBottomSheetDialogFragment<DialogEditProfileBinding> {

    @Deprecated(DEPRECATION_MESSAGE, level = DeprecationLevel.ERROR)
    constructor()

    constructor(launcher: EditProfileLauncher) {
        setLauncher(launcher)
    }

    companion object {
        fun show(fm: FragmentManager) {
            EditProfileDialogFragment(
                EditProfileLauncher(Text.from(R.string.profile_edit_editProfileTitle))
            ).show(
                fm,
                EditProfileDialogFragment::class.java.canonicalName
            )
        }

        fun show(launcher: EditProfileLauncher, fm: FragmentManager) {
            EditProfileDialogFragment(launcher).show(
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

        viewModel.profileTitle.observe(viewLifecycleOwner, binding.tvTextChangeTitle::setText)
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