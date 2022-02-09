package io.flaterlab.meshexam.examination.ui.question.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import io.flaterlab.meshexam.androidbase.ext.applyLayoutParams
import io.flaterlab.meshexam.examination.R
import io.flaterlab.meshexam.examination.databinding.ItemExamAnswerBinding
import io.flaterlab.meshexam.examination.dvo.AnswerDvo
import io.flaterlab.meshexam.uikit.ext.getColorAttr
import javax.inject.Inject


internal class AnswerListAdapter @Inject constructor(

) : ListAdapter<AnswerDvo, AnswerListAdapter.ViewHolder>(DIFF_CALLBACK) {

    var onAnswerSelectedListener: (AnswerDvo) -> Unit = {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(parent).apply {
            itemView.setOnClickListener {
                runCatching { getItem(adapterPosition) }.onSuccess(onAnswerSelectedListener)
            }
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
        holder.itemView.applyLayoutParams<RecyclerView.LayoutParams> {
            bottomMargin =
                if (position == itemCount - 1) {
                    holder.itemView
                        .resources.getDimension(R.dimen.margin_bottom_with_button).toInt()
                } else {
                    0
                }
        }
    }

    class ViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.item_exam_answer, parent, false)
    ) {
        private val binding = ItemExamAnswerBinding.bind(itemView)

        fun bind(item: AnswerDvo) = with(binding) {
            val variant = "${'a' + adapterPosition}."
            tvAnswerVariant.text = variant
            tvAnswerContent.text = item.answer
            itemView.setBackgroundResource(
                if (item.isSelected) {
                    R.drawable.background_exam_answer_selected
                } else {
                    R.drawable.background_exam_answer
                }
            )
            tvAnswerVariant.setTextColor(
                itemView.context.getColorAttr(
                    if (item.isSelected) R.attr.colorSecondary else R.attr.colorOnBackgroundLight
                )
            )
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<AnswerDvo>() {

            override fun areContentsTheSame(oldItem: AnswerDvo, newItem: AnswerDvo): Boolean {
                return false
            }

            override fun areItemsTheSame(oldItem: AnswerDvo, newItem: AnswerDvo): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }
}