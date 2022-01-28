package io.flaterlab.meshexam.create.ui.details

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import io.flaterlab.meshexam.androidbase.ViewBindingFragment
import io.flaterlab.meshexam.androidbase.ViewBindingProvider
import io.flaterlab.meshexam.create.databinding.FragmentQuestionDetailsBinding
import timber.log.Timber

@AndroidEntryPoint
internal class QuestionDetailsFragment : ViewBindingFragment<FragmentQuestionDetailsBinding> {

    @Deprecated("Use constructor with launcher", level = DeprecationLevel.ERROR)
    constructor() : super()

    constructor(launcher: QuestionDetailsLauncher) : super() {
        arguments = bundleOf(
            LAUNCHER to launcher
        )
        Timber.d("Created...")
    }

    private val viewModel: QuestionDetailsViewModel by viewModels()

    override val viewBinder: ViewBindingProvider<FragmentQuestionDetailsBinding>
        get() = FragmentQuestionDetailsBinding::inflate

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.tvQuestion.text = viewModel.launcher.questionId
    }
}