package io.flaterlab.meshexam.uikit.ext

import android.content.Context
import android.util.TypedValue
import androidx.annotation.AttrRes
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat

fun Context.getColorCompat(@ColorRes res: Int) = ContextCompat.getColor(this, res)

fun Context.getColorAttr(@AttrRes res: Int): Int {
    val typedValue = TypedValue()
    theme.resolveAttribute(res, typedValue, true)
    return ContextCompat.getColor(this, typedValue.resourceId)
}