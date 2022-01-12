package io.flaterlab.feature.main.message

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import io.flaterlab.feature.main.R
import io.flaterlab.feature.main.databinding.ItemMessageBinding
import javax.inject.Inject

class MessageAdapter @Inject constructor(

) : RecyclerView.Adapter<MessageAdapter.ViewHolder>() {

    private var messages: List<MessageDvo> = emptyList()

    fun submitList(list: List<MessageDvo>) {
        messages = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindTo(messages[position])
    }

    override fun getItemCount(): Int {
        return messages.size
    }

    class ViewHolder(
        parent: ViewGroup
    ) : RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.item_message, parent, false)
    ) {
        val binding = ItemMessageBinding.bind(itemView)

        fun bindTo(item: MessageDvo) {
            binding.tvText.text = item.text
        }
    }
}