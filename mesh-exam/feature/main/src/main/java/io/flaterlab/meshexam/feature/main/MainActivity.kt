package io.flaterlab.meshexam.feature.main

import android.os.Bundle
import androidx.navigation.fragment.NavHostFragment
import dagger.hilt.android.AndroidEntryPoint
import io.flaterlab.meshexam.androidbase.BaseActivity
import io.flaterlab.meshexam.feature.main.databinding.ActivityMainBinding
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : BaseActivity() {

    @Inject lateinit var navGraphProvider: MainNavGraphProvider
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navController = binding.mainFragmentContainer.getFragment<NavHostFragment>()
            .navController
        val navGraph = navController.navInflater.inflate(navGraphProvider.navGraphId)
        navController.graph = navGraph
    }
}