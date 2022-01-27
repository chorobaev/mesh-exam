package io.flaterlab.meshexam.presentation.di

import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import io.flaterlab.meshexam.androidbase.GlobalNavControllerProvider
import io.flaterlab.meshexam.presentation.MainActivity
import javax.inject.Inject

internal class GlobalNavControllerProviderImpl @Inject constructor(
    private val fragment: Fragment,
) : GlobalNavControllerProvider {

    override fun get(): NavController {
        val activity = fragment.requireActivity() as MainActivity
        return activity.binding.mainFragmentContainer.getFragment<NavHostFragment>().navController
    }
}