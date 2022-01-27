package io.flaterlab.meshexam.presentation

import android.os.Bundle
import dagger.hilt.android.AndroidEntryPoint
import io.flaterlab.meshexam.androidbase.BaseActivity
import io.flaterlab.meshexam.presentation.databinding.ActivityMainBinding

@AndroidEntryPoint
class MainActivity : BaseActivity() {

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_MeshExam)
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}