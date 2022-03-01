package io.flaterlab.meshexam.feature.meshroom.ui.monitor

import android.os.Bundle
import android.view.View
import androidx.activity.addCallback
import androidx.navigation.fragment.findNavController
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import io.flaterlab.meshexam.androidbase.ViewBindingFragment
import io.flaterlab.meshexam.androidbase.ViewBindingProvider
import io.flaterlab.meshexam.androidbase.ext.clickWithDebounce
import io.flaterlab.meshexam.androidbase.ext.showAlert
import io.flaterlab.meshexam.feature.meshroom.R
import io.flaterlab.meshexam.feature.meshroom.databinding.FragmentMonitorBinding
import io.flaterlab.meshexam.feature.meshroom.ui.monitor.adapter.MonitorPageAdapter
import javax.inject.Inject
import javax.inject.Provider

@AndroidEntryPoint
internal class MonitorFragment : ViewBindingFragment<FragmentMonitorBinding>() {

    private val viewModel: MonitorViewModel by vm()

    @Inject
    lateinit var monitorPagerAdapterProvider: Provider<MonitorPageAdapter>
    private val pagerAdapter get() = binding.viewPagerMonitor.adapter as MonitorPageAdapter

    override val viewBinder: ViewBindingProvider<FragmentMonitorBinding>
        get() = FragmentMonitorBinding::inflate

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            viewModel.onBackPressed()
        }

        initViewPager()

        viewModel.exam.observe(viewLifecycleOwner) { exam ->
            binding.toolbarMonitor.run {
                setTitle(exam.name)
                setSubtitle(exam.type)
            }
        }
        viewModel.timer.observe(viewLifecycleOwner, binding.tvExamTimer::setText)
        viewModel.commandShowFinishPrompt.observe(viewLifecycleOwner) {
            showAlert(
                message = getString(R.string.monitor_finishConfirmMessage),
                positive = getString(R.string.common_yes),
                negative = getString(R.string.common_no),
                positiveCallback = { viewModel.onFinishConfirmed() }
            )
        }
        viewModel.commandFinishExam.observe(viewLifecycleOwner) {
            // TODO: implement a navigation to result screen
            findNavController().popBackStack()
        }

        binding.btnFinish.clickWithDebounce(action = viewModel::onFinishClicked)
    }

    private fun initViewPager() {
        binding.viewPagerMonitor.adapter = monitorPagerAdapterProvider.get()

        TabLayoutMediator(binding.tabLayoutMonitor, binding.viewPagerMonitor) { tab, position ->
            tab.setText(pagerAdapter.items[position].first)
        }.attach()
    }
}