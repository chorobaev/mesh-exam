package io.flaterlab.meshexam.feature.meshroom.ui.list

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import io.flaterlab.meshexam.androidbase.TextWatcherManager
import io.flaterlab.meshexam.androidbase.ViewBindingFragment
import io.flaterlab.meshexam.androidbase.ViewBindingProvider
import io.flaterlab.meshexam.androidbase.bindTextWatcher
import io.flaterlab.meshexam.androidbase.common.adapter.ClientListAdapter
import io.flaterlab.meshexam.feature.meshroom.databinding.FragmentStudentListMonitorBinding
import javax.inject.Inject

@AndroidEntryPoint
internal class StudentListMonitorFragment :
    ViewBindingFragment<FragmentStudentListMonitorBinding>() {

    private val viewModel: StudentListMonitorViewModel by vm()
    private val watcherManager = TextWatcherManager(this)

    @Inject
    lateinit var studentListAdapter: ClientListAdapter

    override val viewBinder: ViewBindingProvider<FragmentStudentListMonitorBinding>
        get() = FragmentStudentListMonitorBinding::inflate

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecyclerView()
        binding.etStudentListSearch.bindTextWatcher(watcherManager) { text ->
            viewModel.onSearchTextChanged(text?.toString())
        }

        viewModel.studentListState.observe(
            viewLifecycleOwner,
            binding.recyclerViewStudentList::setState
        )
        viewModel.studentList.observe(viewLifecycleOwner, studentListAdapter::submitList)
    }

    private fun initRecyclerView() = with(binding.recyclerViewStudentList) {
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        adapter = studentListAdapter
    }
}