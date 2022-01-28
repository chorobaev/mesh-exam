package io.flaterlab.meshexam.androidbase

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel

abstract class BaseViewModel : ViewModel() {
}

fun <T> SavedStateHandle.getLauncher(): T {
    return get<T>(BaseFragment.LAUNCHER) ?: throw IllegalStateException("Launcher must not be null")
}