package io.flaterlab.meshexam.feature.meshroom.ui.list

import android.os.Bundle
import android.view.View
import dagger.hilt.android.AndroidEntryPoint
import io.flaterlab.meshexam.androidbase.ViewBindingFragment
import io.flaterlab.meshexam.androidbase.ViewBindingProvider
import io.flaterlab.meshexam.feature.meshroom.databinding.FragmentStudentListMonitorBinding

@AndroidEntryPoint
internal class StudentListMonitorFragment :
    ViewBindingFragment<FragmentStudentListMonitorBinding>() {

    private val viewModel: StudentListMonitorViewModel by vm()

    override val viewBinder: ViewBindingProvider<FragmentStudentListMonitorBinding>
        get() = FragmentStudentListMonitorBinding::inflate

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }
}