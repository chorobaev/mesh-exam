package io.flaterlab.meshexam.uikit.view

import android.content.Context
import android.os.Bundle
import android.os.Parcelable
import android.text.InputType
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.inputmethod.EditorInfo
import android.widget.FrameLayout
import io.flaterlab.meshexam.androidbase.text.Text
import io.flaterlab.meshexam.androidbase.text.resolve
import io.flaterlab.meshexam.uikit.R
import io.flaterlab.meshexam.uikit.databinding.ViewTitledTextInputBinding
import io.flaterlab.meshexam.uikit.ext.obtainStyledAttributes

class TitledTextInput : FrameLayout {

    constructor(context: Context) : super(context) {
        init(null)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleRes: Int) : super(
        context,
        attrs,
        defStyleRes
    ) {
        init(attrs, defStyleRes)
    }

    private val binding = ViewTitledTextInputBinding
        .inflate(LayoutInflater.from(context), this, false)
        .also { addView(it.root) }
    val textInputLayout get() = binding.etFrameInput
    val editText get() = binding.etInput
    val titleTextView get() = binding.tvInputTitle

    private fun init(attrs: AttributeSet?, defStyleRes: Int = 0) {
        attrs.obtainStyledAttributes(context, R.styleable.TitledTextInput, defStyleRes) {
            binding.tvInputTitle.text =
                it.getString(R.styleable.TitledTextInput_title) ?: ""
            binding.etInput.hint =
                it.getString(R.styleable.TitledTextInput_android_hint) ?: ""
            binding.etInput.inputType =
                it.getInt(R.styleable.TitledTextInput_android_inputType, InputType.TYPE_CLASS_TEXT)
            binding.etInput.imeOptions =
                it.getInt(
                    R.styleable.TitledTextInput_android_imeOptions,
                    EditorInfo.TYPE_TEXT_VARIATION_NORMAL
                )
        }
    }

    override fun onSaveInstanceState(): Parcelable {
        return Bundle().apply {
            putString(State.INPUT_KEY, editText.text?.toString())
            putParcelable(State.SUPER_STATE_KEY, super.onSaveInstanceState())
        }
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        var superState: Parcelable? = state
        if (state is Bundle) {
            editText.setText(state.getString(State.INPUT_KEY))
            superState = state.getParcelable(State.SUPER_STATE_KEY)
        }
        super.onRestoreInstanceState(superState)
    }

    private object State {
        const val SUPER_STATE_KEY = "super_state"
        const val INPUT_KEY = "is_rated"
    }
}

fun TitledTextInput.setError(text: Text) {
    textInputLayout.isErrorEnabled = !text.isEmpty
    textInputLayout.error = text.resolve(context)
}