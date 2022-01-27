package io.flaterlab.meshexam.androidbase

import android.text.Editable
import android.text.TextWatcher
import android.widget.TextView
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer

class TextWatcherManager(fragment: Fragment) {

    private val textWatchers: MutableMap<Int, Pair<TextView, TextWatcher>> = mutableMapOf()

    init {
        fragment.lifecycle.addObserver(object : DefaultLifecycleObserver {
            val viewLifecycleOwnerLiveDataObserver = Observer<LifecycleOwner?> {
                val viewLifecycleOwner = it ?: return@Observer

                viewLifecycleOwner.lifecycle.addObserver(object : DefaultLifecycleObserver {
                    override fun onDestroy(owner: LifecycleOwner) {
                        textWatchers.values.forEach { (view, watcher) ->
                            view.removeTextChangedListener(watcher)
                        }
                        textWatchers.clear()
                    }
                })
            }

            override fun onCreate(owner: LifecycleOwner) {
                fragment.viewLifecycleOwnerLiveData.observeForever(
                    viewLifecycleOwnerLiveDataObserver
                )
            }

            override fun onDestroy(owner: LifecycleOwner) {
                fragment.viewLifecycleOwnerLiveData.removeObserver(
                    viewLifecycleOwnerLiveDataObserver
                )
            }
        })
    }

    fun addWatcher(textView: TextView, watcher: TextWatcher) {
        textWatchers[textView.id] = textView to watcher
    }

    fun removeWatcher(textView: TextView, watcher: TextWatcher) {
        textWatchers.remove(textView.id)
        textView.removeTextChangedListener(watcher)
    }
}

fun TextView.bindTextWatcher(manager: TextWatcherManager, onText: (Editable?) -> Unit) {
    manager.addWatcher(this, addTextChangedListener(afterTextChanged = onText))
}