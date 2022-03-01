package io.flaterlab.meshexam.feature.meshroom.ui.event.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import io.flaterlab.meshexam.feature.meshroom.R
import io.flaterlab.meshexam.feature.meshroom.databinding.ItemMonitorEventBinding
import io.flaterlab.meshexam.feature.meshroom.dvo.EventDvo
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

internal class EventListAdapter @Inject constructor(

) : ListAdapter<EventDvo, EventListAdapter.ViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindTo(getItem(position))
    }

    class ViewHolder(
        parent: ViewGroup
    ) : RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.item_monitor_event, parent, false)
    ) {

        val binding = ItemMonitorEventBinding.bind(itemView)

        fun bindTo(item: EventDvo) = with(binding) {
            tvMonitorEventTitle.text = item.title
            tvMonitorEventOwner.text = item.owner
            tvMonitorEventTime.text = Date(item.timeInMillis).let {
                SimpleDateFormat("hh:mm", Locale.ROOT).format(it)
            }
            ivMonitorEventActiveness.isVisible = item.isActive
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<EventDvo>() {

            override fun areContentsTheSame(oldItem: EventDvo, newItem: EventDvo): Boolean {
                return oldItem == newItem
            }

            override fun areItemsTheSame(oldItem: EventDvo, newItem: EventDvo): Boolean {
                return oldItem == newItem
            }
        }
    }
}