package io.flaterlab.meshexam.presentation.profile.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import io.flaterlab.meshexam.android_base.databinding.ItemExamCardBinding
import io.flaterlab.meshexam.androidbase.ext.applyLayoutParams
import io.flaterlab.meshexam.androidbase.ext.clickWithDebounce
import io.flaterlab.meshexam.presentation.profile.R
import io.flaterlab.meshexam.presentation.profile.dvo.HistoryDvo
import io.flaterlab.meshexam.uikit.ext.getColorAttr
import javax.inject.Inject

internal class HistoryListAdapter @Inject constructor(

) : ListAdapter<HistoryDvo, HistoryListAdapter.ViewHolder>(DIF_CALLBACK) {

    var onItemClickListener: (HistoryDvo) -> Unit = {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(parent).apply {
            itemView.clickWithDebounce {
                runCatching { getItem(adapterPosition) }.onSuccess(onItemClickListener)
            }
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
        holder.itemView.applyLayoutParams<RecyclerView.LayoutParams> {
            bottomMargin = if (position == itemCount - 1) {
                holder.itemView.resources.getDimension(R.dimen.margin_medium).toInt()
            } else {
                0
            }
        }
    }

    class ViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.item_exam_card, parent, false)
    ) {

        val binding = ItemExamCardBinding.bind(itemView)

        init {
            binding.tvExamStatus.setText(R.string.profile_main_hosted)
        }

        fun bind(item: HistoryDvo) = with(binding) {
            tvExamName.text = item.name
            tvExamDuration.text = itemView.context.getString(
                io.flaterlab.meshexam.android_base.R.string.common_string_min,
                item.durationInMin.toString()
            )
            cvExamItem.setCardBackgroundColor(
                itemView.context.getColorAttr(
                    if (item.isHosted) R.attr.colorSurface else android.R.attr.colorBackground
                )
            )
            tvExamStatus.isEnabled = item.isHosted
        }
    }

    companion object {

        private val DIF_CALLBACK = object : DiffUtil.ItemCallback<HistoryDvo>() {
            override fun areContentsTheSame(oldItem: HistoryDvo, newItem: HistoryDvo): Boolean {
                return oldItem == newItem
            }

            override fun areItemsTheSame(oldItem: HistoryDvo, newItem: HistoryDvo): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }
}