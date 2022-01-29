package io.flaterlab.meshexam.presentation.exams

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dagger.hilt.android.AndroidEntryPoint
import io.flaterlab.meshexam.androidbase.BaseFragment
import io.flaterlab.meshexam.androidbase.common.adapter.ExamListAdapter
import io.flaterlab.meshexam.androidbase.ext.clickWithDebounce
import io.flaterlab.meshexam.presentation.exams.databinding.FragmentExamsBinding
import io.flaterlab.meshexam.presentation.exams.dvo.ExamDvo
import io.flaterlab.meshexam.presentation.exams.router.ExamsRouter
import javax.inject.Inject

@AndroidEntryPoint
internal class ExamsFragment : BaseFragment() {

    private val viewModel: ExamsViewModel by vm()
    private var _binding: FragmentExamsBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var examsRouter: ExamsRouter

    @Inject
    lateinit var examsAdapter: ExamListAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentExamsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecyclerView()
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}