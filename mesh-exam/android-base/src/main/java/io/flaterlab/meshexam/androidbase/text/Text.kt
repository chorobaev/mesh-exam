package io.flaterlab.meshexam.androidbase.text

import android.content.Context
import android.widget.TextView
import androidx.annotation.StringRes
import io.flaterlab.meshexam.uikit.view.TitledTextInput

class Text private constructor(
    val rawText: String? = null,
    @StringRes val resourceText: Int? = null,
) {

    val isEmpty get() = rawText == null && resourceText == null

    companion object {

        fun from(text: String) = Text(rawText = text)

        fun from(@StringRes resId: Int) = Text(resourceText = resId)

        fun empty() = Text()
    }
}

fun TextView.setText(value: Text) {
    text = value.resolve(context)
}

fun Text.resolve(context: Context): String? {
    return when {
        rawText != null -> rawText
        resourceText != null -> context.getString(resourceText)
        else -> null
    }
}

fun TitledTextInput.setError(text: Text) {
    textInputLayout.isErrorEnabled = !text.isEmpty
    textInputLayout.error = text.resolve(context)
}
