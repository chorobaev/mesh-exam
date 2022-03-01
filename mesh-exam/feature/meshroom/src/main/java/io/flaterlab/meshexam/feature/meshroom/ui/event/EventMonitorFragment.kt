package io.flaterlab.meshexam.feature.meshroom.ui.event

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import io.flaterlab.meshexam.androidbase.ViewBindingFragment
import io.flaterlab.meshexam.androidbase.ViewBindingProvider
import io.flaterlab.meshexam.feature.meshroom.databinding.FragmentEventMonitorBinding
import io.flaterlab.meshexam.feature.meshroom.ui.event.adapter.EventListAdapter
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
internal class EventMonitorFragment : ViewBindingFragment<FragmentEventMonitorBinding>() {

    private val viewModel: EventMonitorViewModel by vm()

    @Inject
    lateinit var eventListAdapter: EventListAdapter

    override val viewBinder: ViewBindingProvider<FragmentEventMonitorBinding>
        get() = FragmentEventMonitorBinding::inflate

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecyclerView()

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.eventList.collect(eventListAdapter::submitList)
            }
        }
        viewModel.eventListState.observe(viewLifecycleOwner, binding.recyclerViewEvents::setState)
    }

    private fun initRecyclerView() = with(binding.recyclerViewEvents) {
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        adapter = eventListAdapter
    }
}