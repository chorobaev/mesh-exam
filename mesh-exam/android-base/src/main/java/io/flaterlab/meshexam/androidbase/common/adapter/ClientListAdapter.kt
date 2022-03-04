package io.flaterlab.meshexam.androidbase.common.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.ColorInt
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import io.flaterlab.meshexam.android_base.R
import io.flaterlab.meshexam.android_base.databinding.ItemClientBinding
import io.flaterlab.meshexam.androidbase.ext.applyLayoutParams
import io.flaterlab.meshexam.androidbase.ext.clickWithDebounce
import io.flaterlab.meshexam.androidbase.text.Text
import io.flaterlab.meshexam.androidbase.text.resolve
import io.flaterlab.meshexam.androidbase.text.setText
import javax.inject.Inject

class ClientListAdapter @Inject constructor(

) : ListAdapter<ClientListAdapter.ClientItem, ClientListAdapter.ViewHolder>(DIFF_CALLBACK) {

    var onClientClickListener: (ClientItem) -> Unit = {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(parent).apply {
            itemView.clickWithDebounce {
                runCatching { getItem(adapterPosition) }.onSuccess(onClientClickListener)
            }
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
        holder.itemView.applyLayoutParams<RecyclerView.LayoutParams> {
            bottomMargin = if (position == itemCount - 1) {
                holder.itemView.resources.getDimension(R.dimen.margin_bottom_with_button).toInt()
            } else {
                0
            }
        }
    }

    class ViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.item_client, parent, false)
    ) {

        val binding = ItemClientBinding.bind(itemView)

        fun bind(item: ClientItem) = with(binding) {
            val order = "${adapterPosition + 1}."
            tvClientOrderNumber.text = order
            tvClientName.text = item.fullName
            tvClientInfo.text = item.info.resolve(itemView.context, default = "").ifBlank { "-" }
            tvClientStatus.setText(item.status)
            tvClientStatus.setTextColor(item.provideStatusTextColor(tvClientStatus.context))
            tvClientStatusInfo.text = item.statusInfoProvider(itemView.context)
        }
    }

    interface ClientItem {
        val id: String
        val fullName: String
        val info: Text
        val status: Text
        val statusInfoProvider: (Context) -> String get() = { "" }

        @ColorInt
        fun provideStatusTextColor(context: Context): Int
    }

    companion object {

        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ClientItem>() {
            override fun areItemsTheSame(oldItem: ClientItem, newItem: ClientItem): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: ClientItem, newItem: ClientItem): Boolean {
                return false
            }
        }
    }
}