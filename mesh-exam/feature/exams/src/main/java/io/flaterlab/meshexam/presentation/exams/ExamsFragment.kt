package io.flaterlab.meshexam.presentation.exams

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import io.flaterlab.meshexam.androidbase.BaseFragment
import io.flaterlab.meshexam.androidbase.GlobalNavControllerProvider
import io.flaterlab.meshexam.presentation.exams.databinding.FragmentExamsBinding
import io.flaterlab.meshexam.presentation.exams.router.ExamsRouter
import javax.inject.Inject

@AndroidEntryPoint
internal class ExamsFragment : BaseFragment() {

    private val viewModel: ExamsViewModel by viewModels()
    private var _binding: FragmentExamsBinding? = null
    private val binding get() = _binding!!

    @Inject lateinit var examsRouter: ExamsRouter

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

        viewModel.openCreateAction.observe(viewLifecycleOwner) {
            examsRouter.openCreateExam()
        }

        binding.fabCreate.setOnClickListener {
            viewModel.onCreatePressed()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}