package io.flaterlab.meshexam.create.ui.details.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import io.flaterlab.meshexam.androidbase.ext.applyLayoutParams
import io.flaterlab.meshexam.androidbase.ext.clickWithDebounce
import io.flaterlab.meshexam.create.R
import io.flaterlab.meshexam.create.databinding.ItemAnswerBinding
import io.flaterlab.meshexam.create.dvo.AnswerDvo
import javax.inject.Inject


class AnswerListAdapter @Inject constructor(

) : ListAdapter<AnswerDvo, RecyclerView.ViewHolder>(DIFF_CALLBACK) {

    var onChangeTextListener: (AnswerDvo) -> Unit = {}
    var onCorrectnessChangeListener: (AnswerDvo, Boolean) -> Unit = { _, _ -> }
    var onLongClickListener: (AnswerDvo) -> Unit = {}
    var onAddAnswerClickListener: () -> Unit = {}

    override fun getItemViewType(position: Int): Int {
        return if (position == itemCount - 1) {
            ADD_VIEW_TYPE
        } else {
            super.getItemViewType(position)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            ADD_VIEW_TYPE -> createAddViewHolder(parent)
            else -> createItemViewHolder(parent)
        }
    }

    private fun createAddViewHolder(parent: ViewGroup) = AddViewHolder(parent).apply {
        itemView.clickWithDebounce(action = onAddAnswerClickListener)
    }

    private fun createItemViewHolder(parent: ViewGroup) = ViewHolder(parent = parent)
        .apply {
            val longClickListener = View.OnLongClickListener {
                getCurrentItem(adapterPosition)
                    ?.let(onLongClickListener)
                    ?.let { true } ?: false
            }
            itemView.setOnLongClickListener(longClickListener)
            binding.tvAnswerContent.setOnLongClickListener(longClickListener)
            binding.checkboxCorrectness.setOnLongClickListener(longClickListener)

            binding.tvAnswerContent.clickWithDebounce {
                getCurrentItem(adapterPosition)?.let(onChangeTextListener)
            }
            binding.checkboxCorrectness.setOnClickListener {
                getCurrentItem(adapterPosition)?.let {
                    onCorrectnessChangeListener(it, binding.checkboxCorrectness.isChecked)
                }
            }
        }

    private fun getCurrentItem(position: Int): AnswerDvo? {
        return runCatching { getItem(position) }.getOrNull()
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ViewHolder -> holder.bind(getItem(position))
            is AddViewHolder -> holder.bind()
        }
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

    override fun getItemCount(): Int {
        return super.getItemCount() + 1
    }

    class ViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.item_answer, parent, false)
    ) {

        val binding = ItemAnswerBinding.bind(itemView)

        fun bind(item: AnswerDvo) = with(binding) {
            val variant = "${'a' + adapterPosition}."
            tvAnswerVariant.text = variant
            tvAnswerContent.text = item.content
            checkboxCorrectness.isChecked = item.isCorrect
        }
    }

    class AddViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.item_answer, parent, false)
    ) {

        val binding = ItemAnswerBinding.bind(itemView).apply {
            tvAnswerContent.setHint(R.string.create_create_question_addAnswer)
            checkboxCorrectness.isVisible = false
        }

        fun bind() = with(binding) {
            val variant = "${'a' + adapterPosition}."
            tvAnswerVariant.text = variant
        }
    }

    companion object {
        private const val ADD_VIEW_TYPE = 1

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