package io.flaterlab.meshexam.androidbase

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import io.flaterlab.meshexam.androidbase.text.Text
import timber.log.Timber

abstract class BaseViewModel : ViewModel() {

    val message = SingleLiveEvent<Text>()

    protected fun Throwable.showLocalizedMessage() {
        Timber.e(this)
        localizedMessage
            ?.let(Text::from)
            ?.let(this@BaseViewModel.message::setValue)
    }
}

fun <T> SavedStateHandle.getLauncher(): T {
    return get<T>(BaseFragment.LAUNCHER) ?: throw IllegalStateException("Launcher must not be null")
}