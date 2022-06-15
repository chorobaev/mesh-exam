package io.flaterlab.meshexam.onboarding.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import io.flaterlab.meshexam.androidbase.ViewBindingFragment
import io.flaterlab.meshexam.androidbase.ViewBindingProvider
import io.flaterlab.meshexam.androidbase.ext.clickWithDebounce
import io.flaterlab.meshexam.androidbase.text.Text
import io.flaterlab.meshexam.onboarding.R
import io.flaterlab.meshexam.onboarding.databinding.FragmentOnboardingBinding
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
internal class OnboardingFragment : ViewBindingFragment<FragmentOnboardingBinding>() {

    @Inject
    lateinit var router: OnboardingRouter

    private val viewModel: OnboardingViewModel by viewModels()

    override val viewBinder: ViewBindingProvider<FragmentOnboardingBinding> =
        FragmentOnboardingBinding::inflate

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.userProfileSetUp.collect { isSetUp ->
                    if (isSetUp) {
                        findNavController().popBackStack()
                    }
                }
            }
        }

        binding.btnSetupProfile.clickWithDebounce {
            router.openEditProfile(
                Text.from(R.string.onboarding_getSetUpProfile)
            )
        }

        binding.btnSkipSetup.clickWithDebounce {
            findNavController().popBackStack()
        }
    }
}