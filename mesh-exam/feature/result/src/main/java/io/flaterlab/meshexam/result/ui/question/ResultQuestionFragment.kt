package io.flaterlab.meshexam.result.ui.question

import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import dagger.hilt.android.AndroidEntryPoint
import io.flaterlab.meshexam.androidbase.ViewBindingFragment
import io.flaterlab.meshexam.androidbase.ViewBindingProvider
import io.flaterlab.meshexam.androidbase.ext.applyLayoutParams
import io.flaterlab.meshexam.androidbase.setLauncher
import io.flaterlab.meshexam.result.R
import io.flaterlab.meshexam.result.databinding.FragmentResultQuestionBinding
import io.flaterlab.meshexam.result.databinding.ItemResultAnswerBinding
import io.flaterlab.meshexam.result.dvo.ResultAnswerDvo

@AndroidEntryPoint
internal class ResultQuestionFragment : ViewBindingFragment<FragmentResultQuestionBinding> {

    constructor(launcher: ResultQuestionLauncher) {
        setLauncher(launcher)
    }

    @Deprecated(level = DeprecationLevel.ERROR, message = DEPRECATION_MESSAGE)
    constructor()

    private val viewModel: ResultQuestionViewModel by vm()

    override val viewBinder: ViewBindingProvider<FragmentResultQuestionBinding>
        get() = FragmentResultQuestionBinding::inflate

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.question.observe(viewLifecycleOwner) { dvo ->
            with(binding) {
                tvQuestion.text = dvo.question
                onAnswersReceived(dvo.answers)
            }
        }
    }

    private fun onAnswersReceived(questions: List<ResultAnswerDvo>) {
        binding.containerAnswers.removeAllViews()
        questions.forEachIndexed { index, result ->
            ItemResultAnswerBinding.inflate(layoutInflater, binding.containerAnswers, true)
                .apply {
                    tvAnswerVariant.text = 'a'.plus(index).toString()
                    tvAnswerContent.text = result.answer
                    containerResultAnswer.setBackgroundResource(
                        if (result.isSelected) {
                            R.drawable.background_result_answer_correct
                        } else {
                            R.drawable.background_result_answer
                        }
                    )
                    if (result.isCorrect) {
                        ivAnswerCorrectness.setImageResource(R.drawable.ic_checkbox_checked)
                    }
                    if (index == questions.lastIndex) {
                        containerResultAnswer.applyLayoutParams<LinearLayout.LayoutParams> {
                            bottomMargin = resources.getDimension(R.dimen.margin_medium).toInt()
                        }
                    }
                }
        }
    }
}