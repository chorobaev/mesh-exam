package io.flaterlab.meshexam.examination.ui.join

import android.animation.ValueAnimator
import android.animation.ValueAnimator.INFINITE
import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import io.flaterlab.meshexam.androidbase.ViewBindingFragment
import io.flaterlab.meshexam.androidbase.ViewBindingProvider
import io.flaterlab.meshexam.androidbase.toBundleArgs
import io.flaterlab.meshexam.examination.ExamLauncher
import io.flaterlab.meshexam.examination.R
import io.flaterlab.meshexam.examination.databinding.FragmentJoinExamBinding

@AndroidEntryPoint
internal class JoinExamFragment : ViewBindingFragment<FragmentJoinExamBinding>() {

    private val viewModel: JoinExamViewModel by vm()

    private var loadingAnimator: ValueAnimator? = null

    override val viewBinder: ViewBindingProvider<FragmentJoinExamBinding>
        get() = FragmentJoinExamBinding::inflate

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initAnimation()
        viewModel.commandConnected.observe(viewLifecycleOwner) { examId ->
            findNavController().navigate(
                R.id.action_joinExamFragment_to_joinedFragment,
                ExamLauncher(examId).toBundleArgs()
            )
        }
        viewModel.commandConnectionFailed.observe(viewLifecycleOwner) {
            findNavController().popBackStack()
        }
    }

    private fun initAnimation() {
        loadingAnimator = ValueAnimator.ofInt(0, LOADING_DOT_COUNT).apply {
            repeatCount = INFINITE
            duration = FRAME_CHANGE_DURATION
            addUpdateListener { valueAnimator ->
                val dotCount = valueAnimator.animatedValue as Int
                if (dotCount < LOADING_DOT_COUNT) {
                    binding.tvJoiningDots.text = buildString { repeat(dotCount) { append('.') } }
                }
            }
            start()
        }
    }

    override fun onDestroyView() {
        loadingAnimator?.cancel()
        loadingAnimator = null
        super.onDestroyView()
    }

    companion object {
        private const val FRAME_CHANGE_DURATION = 2000L
        private const val LOADING_DOT_COUNT = 4
    }
}