package io.flaterlab.meshexam.uikit.ext

import android.content.ContextWrapper
import android.view.View
import androidx.appcompat.app.AppCompatActivity

val View.activity: AppCompatActivity?
    get() {
        var context = context
        while (context is ContextWrapper) {
            if (context is AppCompatActivity) {
                return context
            }
            context = context.baseContext
        }
        return null
    }