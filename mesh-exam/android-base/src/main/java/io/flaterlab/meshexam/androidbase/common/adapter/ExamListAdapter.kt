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

) : ListAdapter<ExamListAdapter.ExamItem, RecyclerView.ViewHolder>(DIF_CALLBACK) {

    var onExamClickListener: (ExamItem) -> Unit = {}

    override fun submitList(list: List<ExamItem>?) {
        super.submitList(if (list.isNullOrEmpty()) listOf(ExamItem.Companion.Empty) else list)
    }

    override fun submitList(list: List<ExamItem>?, commitCallback: Runnable?) {
        super.submitList(
            if (list.isNullOrEmpty()) listOf(ExamItem.Companion.Empty) else list,
            commitCallback
        )
    }

    override fun getItemViewType(position: Int): Int {
        return when {
            getItem(position).id == ExamItem.EMPTY_ITEM_ID -> VIEW_TYPE_EMPTY
            else -> super.getItemViewType(position)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_EMPTY -> SimpleViewHolder(parent, R.layout.item_empty)
            else -> ViewHolder(parent).apply {
                itemView.clickWithDebounce {
                    runCatching { getItem(adapterPosition) }.getOrNull()?.let(onExamClickListener)
                }
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ViewHolder -> {
                holder.bind(getItem(position))
                holder.itemView.applyLayoutParams<RecyclerView.LayoutParams> {
                    bottomMargin = if (position == itemCount - 1) {
                        holder.itemView.resources.getDimension(R.dimen.margin_medium).toInt()
                    } else {
                        0
                    }
                }
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

    class SimpleViewHolder(parent: ViewGroup, layoutResId: Int) : RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context).inflate(layoutResId, parent, false)
    )

    interface ExamItem {
        val id: String
        val name: String
        val durationInMin: Long

        companion object {
            internal const val EMPTY_ITEM_ID = "EMPTY_ITEM_ID"

            internal object Empty : ExamItem {
                override val id: String = EMPTY_ITEM_ID
                override val durationInMin: Long = 0
                override val name: String = ""
            }
        }
    }

    companion object {
        private const val VIEW_TYPE_EMPTY = 1

        private val DIF_CALLBACK = object : DiffUtil.ItemCallback<ExamItem>() {
            override fun areItemsTheSame(oldItem: ExamItem, newItem: ExamItem): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: ExamItem, newItem: ExamItem): Boolean {
                return oldItem.id == newItem.id &&
                        oldItem.name == newItem.name &&
                        oldItem.durationInMin == newItem.durationInMin
            }
        }
    }
}