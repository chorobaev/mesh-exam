package io.flaterlab.meshexam.androidbase.common.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import io.flaterlab.meshexam.android_base.R
import io.flaterlab.meshexam.android_base.databinding.ItemExamCardBinding
import io.flaterlab.meshexam.androidbase.ext.applyLayoutParams
import io.flaterlab.meshexam.androidbase.ext.clickWithDebounce
import javax.inject.Inject

class ExamListAdapter @Inject constructor(

) : ListAdapter<ExamListAdapter.ExamItem, ExamListAdapter.ViewHolder>(DIF_CALLBACK) {

    var onExamClickListener: ((ExamItem) -> Unit)? = null
    var onExamLongClickListener: ((ExamItem) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(parent).apply {
            itemView.clickWithDebounce {
                runCatching {
                    getItem(adapterPosition)
                }.getOrNull()?.let { exam ->
                    onExamClickListener?.invoke(exam)
                }
            }
            itemView.setOnLongClickListener {
                runCatching {
                    getItem(adapterPosition)
                }.getOrNull()?.let { exam ->
                    onExamLongClickListener?.invoke(exam)
                    true
                } ?: false
            }
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
        holder.itemView.applyLayoutParams<RecyclerView.LayoutParams> {
            bottomMargin = if (position == itemCount - 1) {
                holder.itemView.resources.getDimension(R.dimen.margin_medium).toInt()
            } else {
                0
            }
        }
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
        val id: String
        val name: String
        val durationInMin: Int
    }

    companion object {
        private val DIF_CALLBACK = object : DiffUtil.ItemCallback<ExamItem>() {
            override fun areItemsTheSame(oldItem: ExamItem, newItem: ExamItem): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: ExamItem, newItem: ExamItem): Boolean {
                return false
            }
        }
    }
}