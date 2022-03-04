package io.flaterlab.meshexam.feature.meshroom.ui.result

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import io.flaterlab.meshexam.androidbase.ViewBindingFragment
import io.flaterlab.meshexam.androidbase.ViewBindingProvider
import io.flaterlab.meshexam.androidbase.ext.clickWithDebounce
import io.flaterlab.meshexam.feature.meshroom.R
import io.flaterlab.meshexam.feature.meshroom.databinding.FragmentHostResultBinding
import io.flaterlab.meshexam.feature.meshroom.router.MeshroomRouter
import javax.inject.Inject

@AndroidEntryPoint
internal class HostResultFragment : ViewBindingFragment<FragmentHostResultBinding>() {

    private val viewModel: HostResultViewModel by vm()

    @Inject
    lateinit var meshroomRouter: MeshroomRouter

    override val viewBinder: ViewBindingProvider<FragmentHostResultBinding>
        get() = FragmentHostResultBinding::inflate

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.resultInfo.observe(viewLifecycleOwner) { (time, submissionCount) ->
            with(binding) {
                tvExamResultTime.text = time
                tvExamResultSubmitted.text = submissionCount.toString()
            }
        }
        viewModel.commandOpenResults.observe(viewLifecycleOwner) { attemptId ->
            meshroomRouter.openResultsScreen(attemptId)
        }
        viewModel.commandGoToMain.observe(viewLifecycleOwner) {
            findNavController().popBackStack(R.id.nav_mesh, true)
        }

        binding.btnBackToMain.clickWithDebounce(action = viewModel::onGoToMainClicked)

        binding.btnSeeResults.clickWithDebounce(action = viewModel::onSeeResultsClicked)
    }
}