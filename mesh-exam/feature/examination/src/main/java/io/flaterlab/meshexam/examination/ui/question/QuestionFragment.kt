package io.flaterlab.meshexam.examination.ui.question

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.SimpleItemAnimator
import dagger.hilt.android.AndroidEntryPoint
import io.flaterlab.meshexam.androidbase.ViewBindingFragment
import io.flaterlab.meshexam.androidbase.ViewBindingProvider
import io.flaterlab.meshexam.androidbase.setLauncher
import io.flaterlab.meshexam.examination.databinding.FragmentQuestionBinding
import io.flaterlab.meshexam.examination.ui.question.adapter.AnswerListAdapter
import javax.inject.Inject

@AndroidEntryPoint
internal class QuestionFragment : ViewBindingFragment<FragmentQuestionBinding> {

    @Deprecated(DEPRECATION_MESSAGE, level = DeprecationLevel.ERROR)
    constructor()

    constructor(launcher: QuestionLauncher) {
        setLauncher(launcher)
    }

    @Inject
    lateinit var answerAdapter: AnswerListAdapter

    private val viewModel: QuestionViewModel by vm()

    override val viewBinder: ViewBindingProvider<FragmentQuestionBinding>
        get() = FragmentQuestionBinding::inflate

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAnswerRecyclerView()

        viewModel.question.observe(viewLifecycleOwner, binding.tvQuestion::setText)
        viewModel.answers.observe(viewLifecycleOwner, answerAdapter::submitList)
    }

    private fun initAnswerRecyclerView() = with(binding.recyclerViewQuestions) {
        (itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
        adapter = answerAdapter
        answerAdapter.onAnswerSelectedListener = viewModel::onAnswerClicked
    }
}