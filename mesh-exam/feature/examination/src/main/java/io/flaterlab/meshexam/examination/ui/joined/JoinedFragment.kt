package io.flaterlab.meshexam.examination.ui.joined

import android.os.Bundle
import android.view.View
import androidx.activity.addCallback
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import io.flaterlab.meshexam.androidbase.ViewBindingFragment
import io.flaterlab.meshexam.androidbase.ViewBindingProvider
import io.flaterlab.meshexam.androidbase.ext.showAlert
import io.flaterlab.meshexam.androidbase.toBundleArgs
import io.flaterlab.meshexam.examination.R
import io.flaterlab.meshexam.examination.databinding.FragmentJoinedBinding
import io.flaterlab.meshexam.examination.ui.exam.ExamAttemptLauncher

@AndroidEntryPoint
internal class JoinedFragment : ViewBindingFragment<FragmentJoinedBinding>() {

    private val viewModel: JoinedViewModel by vm()

    override val viewBinder: ViewBindingProvider<FragmentJoinedBinding>
        get() = FragmentJoinedBinding::inflate

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            viewModel.onBackPressed()
        }

        viewModel.examName.observe(viewLifecycleOwner, binding.tvJoinedExamName::setText)
        viewModel.commandExamStarted.observe(viewLifecycleOwner) { (examId, attemptId) ->
            findNavController().navigate(
                R.id.action_joinedFragment_to_examFragment,
                ExamAttemptLauncher(examId, attemptId).toBundleArgs(),
            )
        }
        viewModel.commandShowLeavePrompt.observe(viewLifecycleOwner) {
            showAlert(
                message = getString(R.string.exam_join_leaveConfirmMessage),
                positive = getString(R.string.common_yes),
                negative = getString(R.string.common_no),
                positiveCallback = { viewModel.onLeaveClicked() }
            )
        }
        viewModel.commandLeaveExam.observe(viewLifecycleOwner) {
            findNavController().popBackStack()
        }
    }
}