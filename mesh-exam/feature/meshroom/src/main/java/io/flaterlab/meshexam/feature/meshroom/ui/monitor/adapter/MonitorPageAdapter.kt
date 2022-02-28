package io.flaterlab.meshexam.feature.meshroom.ui.monitor.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import io.flaterlab.meshexam.feature.meshroom.R
import io.flaterlab.meshexam.feature.meshroom.ui.event.EventMonitorFragment
import io.flaterlab.meshexam.feature.meshroom.ui.list.StudentListMonitorFragment
import javax.inject.Inject
import javax.inject.Provider

internal class MonitorPageAdapter @Inject constructor(
    fragment: Fragment
) : FragmentStateAdapter(fragment) {

    val items = listOf<Pair<Int, Provider<out Fragment>>>(
        R.string.monitor_tab_events to Provider { EventMonitorFragment() },
        R.string.monitor_tab_studentList to Provider { StudentListMonitorFragment() },
    )

    override fun createFragment(position: Int): Fragment {
        return items[position].second.get()
    }

    override fun getItemCount(): Int {
        return items.size
    }
}