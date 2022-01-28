package io.flaterlab.meshexam.create.ui.details.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import io.flaterlab.meshexam.androidbase.ext.applyLayoutParams
import io.flaterlab.meshexam.androidbase.ext.clickWithDebounce
import io.flaterlab.meshexam.create.R
import io.flaterlab.meshexam.create.databinding.ItemAnswerBinding
import io.flaterlab.meshexam.create.dvo.AnswerDvo
import javax.inject.Inject


class AnswerListAdapter @Inject constructor(

) : RecyclerView.Adapter<AnswerListAdapter.ViewHolder>() {

    var onChangeTextListener: (AnswerDvo) -> Unit = {}
    var onCorrectnessChangeListener: (AnswerDvo, Boolean) -> Unit = { _, _ -> }
    var onLongClickListener: (AnswerDvo) -> Unit = {}

    private val answers = ArrayList<AnswerDvo>()

    fun submitList(list: List<AnswerDvo>?) {
        val safeList = list ?: emptyList()
        val prevItemCount = answers.size
        answers.clear()
        answers.addAll(safeList)
        notifyItemRangeRemoved(0, prevItemCount)
        notifyItemRangeInserted(0, safeList.size)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(parent = parent).apply {
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
            binding.checkboxCorrectness.clickWithDebounce {
                getCurrentItem(adapterPosition)?.let {
                    onCorrectnessChangeListener(it, binding.checkboxCorrectness.isChecked)
                }
            }
        }
    }

    private fun getCurrentItem(position: Int): AnswerDvo? {
        return runCatching { answers[position] }.getOrNull()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = answers[position]
        holder.bind(item)
        holder.itemView.applyLayoutParams<RecyclerView.LayoutParams> {
            bottomMargin =
                if (position == answers.lastIndex) {
                    holder.itemView.resources.getDimension(R.dimen.margin_medium).toInt()
                } else {
                    0
                }
        }
    }

    override fun getItemCount(): Int {
        return answers.size
    }

    class ViewHolder(
        parent: ViewGroup,
    ) : RecyclerView.ViewHolder(
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
}