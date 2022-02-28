package io.flaterlab.meshexam.feature.meshroom.ui.event

import android.os.Bundle
import android.view.View
import dagger.hilt.android.AndroidEntryPoint
import io.flaterlab.meshexam.androidbase.ViewBindingFragment
import io.flaterlab.meshexam.androidbase.ViewBindingProvider
import io.flaterlab.meshexam.feature.meshroom.databinding.FragmentEventMonitorBinding

@AndroidEntryPoint
internal class EventMonitorFragment : ViewBindingFragment<FragmentEventMonitorBinding>() {

    private val viewModel: EventMonitorViewModel by vm()

    override val viewBinder: ViewBindingProvider<FragmentEventMonitorBinding>
        get() = FragmentEventMonitorBinding::inflate

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }
}