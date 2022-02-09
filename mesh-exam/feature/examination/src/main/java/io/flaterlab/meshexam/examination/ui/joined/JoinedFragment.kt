package io.flaterlab.meshexam.examination.ui.joined

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import io.flaterlab.meshexam.androidbase.ViewBindingFragment
import io.flaterlab.meshexam.androidbase.ViewBindingProvider
import io.flaterlab.meshexam.examination.R
import io.flaterlab.meshexam.examination.databinding.FragmentJoinedBinding

@AndroidEntryPoint
internal class JoinedFragment : ViewBindingFragment<FragmentJoinedBinding>() {

    private val viewModel: JoinedViewModel by vm()

    override val viewBinder: ViewBindingProvider<FragmentJoinedBinding>
        get() = FragmentJoinedBinding::inflate

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.examName.observe(viewLifecycleOwner, binding.tvJoinedExamName::setText)
        viewModel.commandExamStarted.observe(viewLifecycleOwner) {
            findNavController().navigate(
                R.id.action_joinedFragment_to_examFragment
            )
        }
    }
}