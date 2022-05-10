package io.flaterlab.meshexam.create.ui.change

import android.os.Bundle
import android.view.View
import androidx.fragment.app.setFragmentResult
import dagger.hilt.android.AndroidEntryPoint
import io.flaterlab.meshexam.androidbase.ViewBindingBottomSheetDialogFragment
import io.flaterlab.meshexam.androidbase.ViewBindingProvider
import io.flaterlab.meshexam.androidbase.ext.clickWithDebounce
import io.flaterlab.meshexam.androidbase.setLauncher
import io.flaterlab.meshexam.create.databinding.DialogChangeTextBinding

@AndroidEntryPoint
internal class ChangeTextDialogFragment :
    ViewBindingBottomSheetDialogFragment<DialogChangeTextBinding> {

    @Deprecated(DEPRECATION_MESSAGE, level = DeprecationLevel.ERROR)
    constructor()

    constructor(launcher: ChangeTextLauncher) {
        setLauncher(launcher)
    }

    private val launcher
        get() = arguments?.getParcelable(LAUNCHER) as? ChangeTextLauncher
            ?: throw IllegalArgumentException("Launcher is not provided")

    override val isFullScreen: Boolean = true

    override val viewBinder: ViewBindingProvider<DialogChangeTextBinding>
        get() = DialogChangeTextBinding::inflate

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tvTextChangeTitle.setText(launcher.titleResId)
        binding.etText.setText(launcher.text)

        binding.tvSaveChanges.clickWithDebounce {
            setFragmentResult(
                launcher.requestKey,
                launcher.args.apply {
                    putString(TEXT_KEY, binding.etText.text?.toString())
                }
            )
            dismiss()
        }
    }

    companion object {
        const val TEXT_KEY = "TEXT_KEY"
    }
}