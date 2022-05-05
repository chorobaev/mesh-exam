package io.flaterlab.meshexam.presentation.discover.ui.main

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.core.view.setPadding
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import io.flaterlab.meshexam.androidbase.ViewBindingFragment
import io.flaterlab.meshexam.androidbase.ViewBindingProvider
import io.flaterlab.meshexam.androidbase.common.adapter.ExamListAdapter
import io.flaterlab.meshexam.androidbase.ext.clickWithDebounce
import io.flaterlab.meshexam.androidbase.ext.dp
import io.flaterlab.meshexam.permission.MeshPermissionDialogFragment
import io.flaterlab.meshexam.presentation.discover.R
import io.flaterlab.meshexam.presentation.discover.databinding.FragmentDiscoverBinding
import io.flaterlab.meshexam.presentation.discover.dvo.AvailableExamDvo
import io.flaterlab.meshexam.presentation.discover.ui.info.ExamInfoDialogFragment
import io.flaterlab.meshexam.presentation.discover.ui.info.ExamInfoLauncher
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
        viewModel.discovering.observe(viewLifecycleOwner) { discovering ->
            binding.progressExamDiscovery.isVisible =
                viewModel.permissionNeededState.value == false && discovering
            binding.btnRefresh.isVisible =
                viewModel.permissionNeededState.value == false && !discovering

            if (binding.btnRefresh.isVisible) {
                binding.recyclerViewExams.recyclerView.setPadding(0, 0, 0, BOTTOM_PADDING.dp)
            } else {
                binding.recyclerViewExams.recyclerView.setPadding(0)
            }
        }
        viewModel.commandObserveExams.observe(viewLifecycleOwner) { observeExams() }

        viewModel.onPermissionsChanged(
            granted = MeshPermissionDialogFragment.isMeshPermissionsGranted(requireContext()),
            shouldRequest = true
        )
        initClickListeners()
    }

    private fun initRecyclerView() = with(binding.recyclerViewExams) {
        recyclerView.setPadding(0, 0, 0, BOTTOM_PADDING.dp)
        recyclerView.clipToPadding = false
        setEmptyStateText(R.string.discover_main_emptyListHint)
        adapter = examsAdapter
        examsAdapter.onExamClickListener = { viewModel.onExamClicked(it as AvailableExamDvo) }
    }

    private fun showPermissionNeededState(enabled: Boolean) {
        binding.permissionNeededState.isVisible = enabled
        binding.recyclerViewExams.isVisible = !enabled
        binding.progressExamDiscovery.isVisible = !enabled
        binding.btnRefresh.isVisible = !enabled
    }

    private fun observeExams() {
        viewModel.exams.observe(viewLifecycleOwner, examsAdapter::submitList)
        viewModel.examListState.observe(viewLifecycleOwner, binding.recyclerViewExams::setState)
    }

    private fun initClickListeners() {
        binding.permissionNeededState.clickWithDebounce(
            action = viewModel::onRequestPermissionsClicked
        )

        binding.btnRefresh.clickWithDebounce(action = viewModel::onRefreshPressed)
    }

    override fun onStart() {
        super.onStart()
        viewModel.onScreenShown()
    }

    override fun onStop() {
        super.onStop()
        viewModel.onScreenHidden()
    }

    companion object {
        private const val BOTTOM_PADDING = 80
    }
}