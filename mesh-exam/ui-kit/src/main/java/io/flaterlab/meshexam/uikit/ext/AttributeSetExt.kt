package io.flaterlab.meshexam.uikit.ext

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet

fun AttributeSet?.obtainStyledAttributes(
    context: Context,
    styleable: IntArray,
    defStyleRes: Int = 0,
    block: (TypedArray) -> Unit
) {
    if (this == null) return

    val typedArray = context.obtainStyledAttributes(this, styleable, defStyleRes, 0)
    try {
        block(typedArray)
    } finally {
        typedArray.recycle()
    }
}