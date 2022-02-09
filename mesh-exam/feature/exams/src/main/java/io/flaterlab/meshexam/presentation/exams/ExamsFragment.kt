package io.flaterlab.meshexam.presentation.exams

import android.os.Bundle
import android.view.View
import dagger.hilt.android.AndroidEntryPoint
import io.flaterlab.meshexam.androidbase.ViewBindingFragment
import io.flaterlab.meshexam.androidbase.ViewBindingProvider
import io.flaterlab.meshexam.androidbase.common.adapter.ExamListAdapter
import io.flaterlab.meshexam.androidbase.ext.clickWithDebounce
import io.flaterlab.meshexam.presentation.exams.databinding.FragmentExamsBinding
import io.flaterlab.meshexam.presentation.exams.dvo.ExamDvo
import io.flaterlab.meshexam.presentation.exams.router.ExamsRouter
import javax.inject.Inject

@AndroidEntryPoint
internal class ExamsFragment : ViewBindingFragment<FragmentExamsBinding>() {

    private val viewModel: ExamsViewModel by vm()

    @Inject
    lateinit var examsRouter: ExamsRouter

    @Inject
    lateinit var examsAdapter: ExamListAdapter

    override val viewBinder: ViewBindingProvider<FragmentExamsBinding>
        get() = FragmentExamsBinding::inflate

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()

        viewModel.examListState.observe(viewLifecycleOwner, binding.recyclerViewExams::setState)
        viewModel.exams.observe(viewLifecycleOwner, examsAdapter::submitList)
        viewModel.openCreateCommand.observe(viewLifecycleOwner) {
            examsRouter.openCreateExam()
        }
        viewModel.openExamCommand.observe(viewLifecycleOwner) { exam ->
            examsRouter.openEditExam(exam.id)
        }

        binding.fabCreate.clickWithDebounce(action = viewModel::onCreatePressed)
    }

    private fun initRecyclerView() = with(binding.recyclerViewExams) {
        adapter = examsAdapter
        examsAdapter.onExamClickListener = { viewModel.onExamPressed(it as ExamDvo) }
    }
}