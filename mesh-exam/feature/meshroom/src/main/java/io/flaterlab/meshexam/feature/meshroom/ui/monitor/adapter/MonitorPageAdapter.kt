package io.flaterlab.meshexam.feature.meshroom.ui.monitor.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import javax.inject.Inject
import javax.inject.Provider

internal class MonitorPageAdapter @Inject constructor(
    fragment: Fragment,
) : FragmentStateAdapter(fragment) {

    var items: List<Pair<Int, Provider<out Fragment>>> = emptyList()
        private set

    fun submitList(list: List<Pair<Int, Provider<out Fragment>>>) {
        notifyItemRangeRemoved(0, items.size)
        items = list
        notifyItemRangeInserted(0, items.size)
    }

    override fun createFragment(position: Int): Fragment {
        return items[position].second.get()
    }

    override fun getItemCount(): Int {
        return items.size
    }
}