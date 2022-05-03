package io.flaterlab.meshexam.onboarding.ui

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import io.flaterlab.meshexam.androidbase.ViewBindingFragment
import io.flaterlab.meshexam.androidbase.ViewBindingProvider
import io.flaterlab.meshexam.androidbase.ext.clickWithDebounce
import io.flaterlab.meshexam.onboarding.databinding.FragmentOnboardingBinding

@AndroidEntryPoint
internal class OnboardingFragment : ViewBindingFragment<FragmentOnboardingBinding>() {

    override val viewBinder: ViewBindingProvider<FragmentOnboardingBinding> =
        FragmentOnboardingBinding::inflate

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnFinishOnboarding.clickWithDebounce {
            findNavController().popBackStack()
        }
    }
}