package io.flaterlab.meshexam.result.ui.send

import android.animation.ValueAnimator
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import io.flaterlab.meshexam.androidbase.ViewBindingFragment
import io.flaterlab.meshexam.androidbase.ViewBindingProvider
import io.flaterlab.meshexam.result.databinding.FragmentSendResultBinding

@AndroidEntryPoint
internal class SendResultFragment : ViewBindingFragment<FragmentSendResultBinding>() {

    private val viewModel: SendResultViewModel by viewModels()

    private var loadingAnimator: ValueAnimator? = null

    override val viewBinder: ViewBindingProvider<FragmentSendResultBinding>
        get() = FragmentSendResultBinding::inflate

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initAnimation()
        viewModel.examName.observe(viewLifecycleOwner, binding.tvSendingExam::setText)
        viewModel.commandResultSent.observe(viewLifecycleOwner) {
            findNavController().popBackStack()
        }
    }

    private fun initAnimation() {
        loadingAnimator = ValueAnimator.ofInt(0, LOADING_DOT_COUNT).apply {
            repeatCount = ValueAnimator.INFINITE
            duration = FRAME_CHANGE_DURATION
            addUpdateListener { valueAnimator ->
                val dotCount = valueAnimator.animatedValue as Int
                if (dotCount < LOADING_DOT_COUNT) {
                    binding.tvSendingDots.text = buildString { repeat(dotCount) { append('.') } }
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