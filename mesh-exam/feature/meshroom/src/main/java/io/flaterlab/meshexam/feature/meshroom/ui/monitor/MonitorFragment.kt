package io.flaterlab.meshexam.feature.meshroom.ui.monitor

import android.os.Bundle
import android.view.View
import dagger.hilt.android.AndroidEntryPoint
import io.flaterlab.meshexam.androidbase.ViewBindingFragment
import io.flaterlab.meshexam.androidbase.ViewBindingProvider
import io.flaterlab.meshexam.feature.meshroom.databinding.FragmentMonitorBinding

@AndroidEntryPoint
internal class MonitorFragment : ViewBindingFragment<FragmentMonitorBinding>() {

    private val viewModel: MonitorViewModel by vm()

    override val viewBinder: ViewBindingProvider<FragmentMonitorBinding>
        get() = FragmentMonitorBinding::inflate

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.exam.observe(viewLifecycleOwner) { exam ->
            binding.toolbarMonitor.run {
                setTitle(exam.name)
                setSubtitle(exam.type)
            }
        }
    }
}