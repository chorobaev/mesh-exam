package io.flaterlab.meshexam.examination.ui.exam

import android.os.Bundle
import android.view.View
import dagger.hilt.android.AndroidEntryPoint
import io.flaterlab.meshexam.androidbase.ViewBindingFragment
import io.flaterlab.meshexam.androidbase.ViewBindingProvider
import io.flaterlab.meshexam.examination.databinding.FragmentJoinExamBinding

@AndroidEntryPoint
internal class ExamFragment : ViewBindingFragment<FragmentJoinExamBinding>() {

    private val viewModel: ExamViewModel by vm()

    override val viewBinder: ViewBindingProvider<FragmentJoinExamBinding>
        get() = FragmentJoinExamBinding::inflate

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }
}