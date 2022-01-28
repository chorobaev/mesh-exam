package io.flaterlab.meshexam.create.ui.details

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import io.flaterlab.meshexam.androidbase.ViewBindingFragment
import io.flaterlab.meshexam.androidbase.ViewBindingProvider
import io.flaterlab.meshexam.androidbase.ext.clickWithDebounce
import io.flaterlab.meshexam.create.R
import io.flaterlab.meshexam.create.databinding.FragmentQuestionDetailsBinding
import io.flaterlab.meshexam.create.ui.change.ChangeTextDialogFragment
import io.flaterlab.meshexam.create.ui.change.ChangeTextLauncher
import io.flaterlab.meshexam.create.ui.details.adapter.AnswerListAdapter
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

    private val viewModel: QuestionDetailsViewModel by viewModels()

    @Inject
    lateinit var answerAdapter: AnswerListAdapter

    override val viewBinder: ViewBindingProvider<FragmentQuestionDetailsBinding>
        get() = FragmentQuestionDetailsBinding::inflate

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecyclerView()
        registerChangeTextResultListeners()

        viewModel.question.observe(viewLifecycleOwner, binding.tvQuestion::setText)
        viewModel.answers.observe(viewLifecycleOwner, answerAdapter::submitList)
        viewModel.deleteAnswerCommand.observe(viewLifecycleOwner) { dvo ->
            Toast.makeText(requireContext(), dvo.content, Toast.LENGTH_SHORT).show()
        }
        viewModel.changeQuestionCommand.observe(viewLifecycleOwner) { question ->
            startChangeDialog(
                ChangeTextLauncher(
                    requestKey = CHANGE_QUESTION_REQUEST_KEY,
                    titleResId = R.string.create_create_question_editQuestion,
                    text = question,
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
        answerAdapter.onChangeTextListener = viewModel::onChangeAnswerTextClicked
        answerAdapter.onCorrectnessChangeListener = viewModel::onChangeAnswerCorrectnessClicked
        answerAdapter.onLongClickListener = viewModel::onAnswerLongClicked
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