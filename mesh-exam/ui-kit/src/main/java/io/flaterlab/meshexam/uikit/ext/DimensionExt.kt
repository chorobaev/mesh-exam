package io.flaterlab.meshexam.androidbase.ext

import android.content.Context

/**
 * Convert dp value to px
 */
fun Int.toPx(context: Context): Int {
    val resources = context.resources
    val metrics = resources.displayMetrics
    return (this * (metrics.densityDpi.toFloat() / 160.0f)).toInt()
}

/**
 * Convert dp value to px
 */
fun Float.toPx(context: Context): Float {
    val resources = context.resources
    val metrics = resources.displayMetrics
    return (this * (metrics.densityDpi.toFloat() / 160.0f))
}