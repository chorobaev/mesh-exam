package io.flaterlab.meshexam.androidbase

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import io.flaterlab.meshexam.androidbase.text.Text

abstract class BaseViewModel : ViewModel() {

    val message = SingleLiveEvent<Text>()
}

fun <T> SavedStateHandle.getLauncher(): T {
    return get<T>(BaseFragment.LAUNCHER) ?: throw IllegalStateException("Launcher must not be null")
}