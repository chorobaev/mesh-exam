package io.flaterlab.meshexam.create.ui.details

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.SimpleItemAnimator
import dagger.hilt.android.AndroidEntryPoint
import io.flaterlab.meshexam.androidbase.ViewBindingFragment
import io.flaterlab.meshexam.androidbase.ViewBindingProvider
import io.flaterlab.meshexam.androidbase.ext.clickWithDebounce
import io.flaterlab.meshexam.create.R
import io.flaterlab.meshexam.create.databinding.FragmentQuestionDetailsBinding
import io.flaterlab.meshexam.create.ui.change.ChangeTextDialogFragment
import io.flaterlab.meshexam.create.ui.change.ChangeTextLauncher
import io.flaterlab.meshexam.create.ui.details.adapter.AnswerListAdapter
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
internal class QuestionDetailsFragment : ViewBindingFragment<FragmentQuestionDetailsBinding> {

    @Deprecated("Use constructor with launcher", level = DeprecationLevel.ERROR)
    constructor() : super()

    constructor(launcher: QuestionDetailsLauncher) : super() {
        arguments = bundleOf(
            LAUNCHER to launcher
        )
    }

    private val viewModel: QuestionDetailsViewModel by vm()

    @Inject
    lateinit var answerAdapter: AnswerListAdapter

    override val viewBinder: ViewBindingProvider<FragmentQuestionDetailsBinding>
        get() = FragmentQuestionDetailsBinding::inflate

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecyclerView()
        registerChangeTextResultListeners()

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                launch {
                    viewModel.question.collectLatest { dvo ->
                        binding.tvQuestion.text = dvo.content
                    }
                }
                launch {
                    viewModel.answers.collectLatest(answerAdapter::submitList)
                }
            }
        }
        viewModel.changeQuestionCommand.observe(viewLifecycleOwner) { question ->
            startChangeDialog(
                ChangeTextLauncher(
                    requestKey = CHANGE_QUESTION_REQUEST_KEY,
                    titleResId = R.string.create_create_question_editQuestion,
                    text = question.content,
                )
            )
        }
        viewModel.changeAnswerCommand.observe(viewLifecycleOwner) { answerDvo ->
            startChangeDialog(
                ChangeTextLauncher(
                    requestKey = CHANGE_ANSWER_REQUEST_KEY,
                    titleResId = R.string.create_create_question_editAnswer,
                    text = answerDvo.content,
                    args = bundleOf(ANSWER_ID_KEY to answerDvo.id),
                )
            )
        }

        binding.tvQuestion.clickWithDebounce(action = viewModel::onChangeQuestionClicked)
    }

    private fun initRecyclerView() = with(binding.recyclerViewQuestions) {
        (itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
        answerAdapter.onChangeTextListener = viewModel::onChangeAnswerTextClicked
        answerAdapter.onCorrectnessChangeListener = viewModel::onChangeAnswerCorrectnessClicked
        answerAdapter.onLongClickListener = viewModel::onAnswerLongClicked
        answerAdapter.onAddAnswerClickListener = viewModel::onAddAnswerClicked
        adapter = answerAdapter
    }

    private fun registerChangeTextResultListeners() = with(childFragmentManager) {
        setFragmentResultListener(CHANGE_QUESTION_REQUEST_KEY, viewLifecycleOwner) { _, bundle ->
            bundle.getString(ChangeTextDialogFragment.TEXT_KEY)?.let(viewModel::onQuestionChanged)
        }

        setFragmentResultListener(CHANGE_ANSWER_REQUEST_KEY, viewLifecycleOwner) { _, bundle ->
            val answerId = bundle.getString(ANSWER_ID_KEY)
            val answerContent = bundle.getString(ChangeTextDialogFragment.TEXT_KEY)

            if (answerId != null && answerContent != null) {
                viewModel.onAnswerChanged(answerId, answerContent)
            }
        }
    }

    private fun startChangeDialog(launcher: ChangeTextLauncher) {
        ChangeTextDialogFragment(launcher)
            .show(childFragmentManager, ChangeTextDialogFragment::class.java.canonicalName)
    }

    companion object {
        private const val CHANGE_QUESTION_REQUEST_KEY = "CHANGE_QUESTION_REQUEST_KEY"
        private const val CHANGE_ANSWER_REQUEST_KEY = "CHANGE_ANSWER_REQUEST_KEY"
        private const val ANSWER_ID_KEY = "ANSWER_ID_KEY"
    }
}