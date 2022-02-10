package io.flaterlab.meshexam.presentation.discover.ui.main

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import io.flaterlab.meshexam.androidbase.ViewBindingFragment
import io.flaterlab.meshexam.androidbase.ViewBindingProvider
import io.flaterlab.meshexam.androidbase.common.adapter.ExamListAdapter
import io.flaterlab.meshexam.androidbase.ext.clickWithDebounce
import io.flaterlab.meshexam.permission.MeshPermissionDialogFragment
import io.flaterlab.meshexam.presentation.discover.databinding.FragmentDiscoverBinding
import io.flaterlab.meshexam.presentation.discover.dvo.AvailableExamDvo
import io.flaterlab.meshexam.presentation.discover.ui.info.ExamInfoDialogFragment
import io.flaterlab.meshexam.presentation.discover.ui.info.ExamInfoLauncher
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class DiscoverFragment : ViewBindingFragment<FragmentDiscoverBinding>() {

    private val viewModel: DiscoverViewModel by viewModels()

    @Inject
    lateinit var examsAdapter: ExamListAdapter

    override val viewBinder: ViewBindingProvider<FragmentDiscoverBinding>
        get() = FragmentDiscoverBinding::inflate

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()

        viewModel.permissionNeededState.observe(viewLifecycleOwner, ::showPermissionNeededState)
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
        viewModel.commandRequestPermission.observe(viewLifecycleOwner) {
            MeshPermissionDialogFragment.show(childFragmentManager, viewModel::onPermissionsChanged)
        }
        viewModel.commandObserveExams.observe(viewLifecycleOwner) { observeExams() }

        viewModel.onPermissionsChanged(
            granted = MeshPermissionDialogFragment.isMeshPermissionsGranted(requireContext()),
            shouldRequest = true
        )
        initClickListeners()
    }

    private fun initRecyclerView() = with(binding.recyclerViewExams) {
        adapter = examsAdapter
        examsAdapter.onExamClickListener = { viewModel.onExamClicked(it as AvailableExamDvo) }
    }

    private fun showPermissionNeededState(enabled: Boolean) {
        binding.recyclerViewExams.isVisible = !enabled
        binding.permissionNeededState.isVisible = enabled
    }

    private fun observeExams() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.exams
                .flowWithLifecycle(viewLifecycleOwner.lifecycle)
                .collectLatest(examsAdapter::submitList)
        }
        viewModel.examListState.observe(viewLifecycleOwner, binding.recyclerViewExams::setState)
    }

    private fun initClickListeners() {
        binding.permissionNeededState.clickWithDebounce(
            action = viewModel::onRequestPermissionsClicked
        )
    }
}