package io.flaterlab.meshexam.create.ui.exam

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import io.flaterlab.meshexam.androidbase.BaseFragment
import io.flaterlab.meshexam.androidbase.TextWatcherManager
import io.flaterlab.meshexam.androidbase.bindTextWatcher
import io.flaterlab.meshexam.androidbase.ext.clickWithDebounce
import io.flaterlab.meshexam.androidbase.toBundleArgs
import io.flaterlab.meshexam.create.R
import io.flaterlab.meshexam.create.databinding.FragmentCreateExamBinding
import io.flaterlab.meshexam.create.ui.question.CreateQuestionLauncher
import io.flaterlab.meshexam.uikit.view.setError

@AndroidEntryPoint
internal class CreateExamFragment : BaseFragment() {

    private val viewModel: CreateExamViewModel by viewModels()
    private var _binding: FragmentCreateExamBinding? = null
    private val binding get() = _binding!!
    private val watcherManager = TextWatcherManager(this)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCreateExamBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.nameError.observe(viewLifecycleOwner, binding.ttiExamName::setError)
        viewModel.typeError.observe(viewLifecycleOwner, binding.ttiExamType::setError)
        viewModel.durationError.observe(viewLifecycleOwner, binding.ttiExamDuration::setError)
        viewModel.nextEnabled.observe(viewLifecycleOwner, binding.btnNext::setEnabled)
        viewModel.openQuestionScreenCommand.observe(viewLifecycleOwner, ::openEditScreen)

        binding.ttiExamName.editText.bindTextWatcher(watcherManager) {
            viewModel.onNameChanged(it?.toString())
        }

        binding.ttiExamType.editText.bindTextWatcher(watcherManager) {
            viewModel.onTypeChanged(it?.toString())
        }

        binding.ttiExamDuration.editText.bindTextWatcher(watcherManager) {
            viewModel.onDurationChanged(it?.toString())
        }

        binding.btnNext.clickWithDebounce(action = viewModel::onNextClicked)
    }

    private fun openEditScreen(examId: String) {
        findNavController().navigate(
            R.id.action_createExamFragment_to_createQuestionFragment,
            CreateQuestionLauncher(examId).toBundleArgs()
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}