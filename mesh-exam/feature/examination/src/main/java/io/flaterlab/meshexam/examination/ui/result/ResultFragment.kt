package io.flaterlab.meshexam.examination.ui.result

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import io.flaterlab.meshexam.androidbase.ViewBindingFragment
import io.flaterlab.meshexam.androidbase.ViewBindingProvider
import io.flaterlab.meshexam.androidbase.ext.clickWithDebounce
import io.flaterlab.meshexam.examination.R
import io.flaterlab.meshexam.examination.databinding.FragmentResultBinding

@AndroidEntryPoint
internal class ResultFragment : ViewBindingFragment<FragmentResultBinding>() {

    private val viewModel: ResultViewModel by viewModels()

    override val viewBinder: ViewBindingProvider<FragmentResultBinding>
        get() = FragmentResultBinding::inflate

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.commandGoToMain.observe(viewLifecycleOwner) {
            findNavController().popBackStack(R.id.nav_examination, true)
        }

        binding.btnBackToMain.clickWithDebounce(action = viewModel::onGoMainClicked)
    }
}