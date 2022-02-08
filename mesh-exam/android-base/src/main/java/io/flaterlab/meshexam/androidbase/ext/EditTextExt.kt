package io.flaterlab.meshexam.androidbase.ext

import android.view.inputmethod.EditorInfo
import android.widget.EditText

fun EditText.setOnDoneClickListener(callback: () -> Unit) {
    setOnEditorActionListener { _, actionId, _ ->
        if (actionId == EditorInfo.IME_ACTION_DONE) {
            callback.invoke()
            return@setOnEditorActionListener true
        }
        false
    }
}