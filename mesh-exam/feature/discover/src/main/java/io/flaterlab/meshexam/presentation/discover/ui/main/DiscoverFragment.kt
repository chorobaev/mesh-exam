package io.flaterlab.meshexam.presentation.discover.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import io.flaterlab.meshexam.androidbase.BaseFragment
import io.flaterlab.meshexam.androidbase.common.adapter.ExamListAdapter
import io.flaterlab.meshexam.presentation.discover.databinding.FragmentDiscoverBinding
import io.flaterlab.meshexam.presentation.discover.dvo.AvailableExamDvo
import io.flaterlab.meshexam.presentation.discover.ui.info.ExamInfoDialogFragment
import io.flaterlab.meshexam.presentation.discover.ui.info.ExamInfoLauncher
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class DiscoverFragment : BaseFragment() {

    private var _binding: FragmentDiscoverBinding? = null
    private val binding get() = _binding!!

    private val viewModel: DiscoverViewModel by viewModels()

    @Inject
    lateinit var examsAdapter: ExamListAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDiscoverBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecyclerView()

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.exams
                .flowWithLifecycle(viewLifecycleOwner.lifecycle)
                .collectLatest(examsAdapter::submitList)
        }

        viewModel.commandOpenExam.observe(viewLifecycleOwner) { exam ->
            ExamInfoDialogFragment.show(
                ExamInfoLauncher(
                    examId = exam.id,
                    examName = exam.name,
                    examDurationInMin = exam.durationInMin,
                    examHostName = exam.hostName
                ),
                childFragmentManager
            )
        }
    }

    private fun initRecyclerView() = with(binding.recyclerViewExams) {
        adapter = examsAdapter

        examsAdapter.onExamClickListener = { viewModel.onExamClicked(it as AvailableExamDvo) }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}