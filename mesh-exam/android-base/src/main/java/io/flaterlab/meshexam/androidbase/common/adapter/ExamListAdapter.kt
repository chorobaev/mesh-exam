package io.flaterlab.meshexam.androidbase.common.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import io.flaterlab.meshexam.android_base.R
import io.flaterlab.meshexam.android_base.databinding.ItemExamCardBinding
import io.flaterlab.meshexam.androidbase.ext.applyLayoutParams
import io.flaterlab.meshexam.androidbase.ext.clickWithDebounce
import javax.inject.Inject

class ExamListAdapter @Inject constructor(

) : RecyclerView.Adapter<ExamListAdapter.ViewHolder>() {

    private val items = ArrayList<ExamItem>()
    var onExamClickListener: (ExamItem) -> Unit = {}

    fun submitList(list: List<ExamItem>) {
        val prevItemCount = list.size
        items.clear()
        items.addAll(list)
        notifyItemRangeRemoved(0, prevItemCount)
        notifyItemRangeInserted(0, list.size)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(parent).apply {
            itemView.clickWithDebounce {
                runCatching { items[adapterPosition] }.getOrNull()?.let(onExamClickListener)
            }
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
        holder.itemView.applyLayoutParams<RecyclerView.LayoutParams> {
            bottomMargin = if (position == items.lastIndex) {
                holder.itemView.resources.getDimension(R.dimen.margin_medium).toInt()
            } else {
                0
            }
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    class ViewHolder(
        parent: ViewGroup
    ) : RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.item_exam_card, parent, false)
    ) {

        val binding = ItemExamCardBinding.bind(itemView)

        fun bind(item: ExamItem) = with(binding) {
            tvExamName.text = item.name
            tvExamDuration.text =
                itemView.context.getString(
                    R.string.common_string_min,
                    item.durationInMin.toString()
                )
        }
    }

    interface ExamItem {
        val name: String
        val durationInMin: Long
    }
}