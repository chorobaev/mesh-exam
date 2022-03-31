package io.flaterlab.meshexam.androidbase.text

import android.content.Context
import android.os.Parcelable
import android.widget.TextView
import androidx.annotation.StringRes
import io.flaterlab.meshexam.uikit.view.TitledTextInput
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

@Parcelize
class Text private constructor(
    val rawText: String? = null,
    @StringRes val resourceText: Int? = null,
    val formatArgs: @RawValue Array<out Any?>? = null,
) : Parcelable {

    val isEmpty get() = rawText == null && resourceText == null

    companion object {

        @JvmStatic
        fun from(text: String) = Text(rawText = text)

        @JvmStatic
        fun from(@StringRes resId: Int) = Text(resourceText = resId)

        /**
         * Use only parcelable primitives as args
         */
        @JvmStatic
        fun from(@StringRes resId: Int, vararg args: Any?) =
            Text(resourceText = resId, formatArgs = args)

        @JvmStatic
        fun empty() = Text()
    }
}

fun TextView.setText(value: Text) {
    text = value.resolve(context)
}

fun Text.resolve(context: Context): String? {
    return when {
        rawText != null -> rawText
        resourceText != null && formatArgs != null -> context.getString(resourceText, *formatArgs)
        resourceText != null -> context.getString(resourceText)
        else -> null
    }
}

fun Text.resolve(context: Context, default: String = ""): String = resolve(context) ?: default

fun TitledTextInput.setError(text: Text) {
    textInputLayout.isErrorEnabled = !text.isEmpty
    textInputLayout.error = text.resolve(context)
}
