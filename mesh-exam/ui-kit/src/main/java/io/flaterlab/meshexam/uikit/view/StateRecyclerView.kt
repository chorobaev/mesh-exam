package io.flaterlab.meshexam.uikit.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.annotation.StringRes
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import io.flaterlab.meshexam.uikit.databinding.ViewStateRecyclerViewBinding

class StateRecyclerView : FrameLayout {

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet?, defStyleRes: Int) : super(
        context,
        attrs,
        defStyleRes
    )

    val recyclerView get() = binding.recyclerView

    var adapter: RecyclerView.Adapter<out RecyclerView.ViewHolder>?
        set(value) {
            binding.recyclerView.adapter = value
        }
        get() = binding.recyclerView.adapter

    private val binding = ViewStateRecyclerViewBinding
        .inflate(LayoutInflater.from(context), this, false)
        .also { addView(it.root) }

    init {
        setState(State.NORMAL)
    }

    fun setState(state: State) = with(binding) {
        recyclerView.isVisible = state == State.NORMAL
        emptyStateLayout.isVisible = state == State.EMPTY
        loadingStateLayout.isVisible = state == State.LOADING
    }

    fun setEmptyStateText(@StringRes resId: Int) {
        binding.tvEmptyText.setText(resId)
    }

    enum class State {
        NORMAL,
        EMPTY,
        LOADING
    }
}