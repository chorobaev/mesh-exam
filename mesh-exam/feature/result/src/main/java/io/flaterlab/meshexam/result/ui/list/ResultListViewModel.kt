package io.flaterlab.meshexam.result.ui.list

import androidx.lifecycle.SavedStateHandle
import dagger.hilt.android.lifecycle.HiltViewModel
import io.flaterlab.meshexam.androidbase.BaseViewModel
import io.flaterlab.meshexam.androidbase.getLauncher
import io.flaterlab.meshexam.result.ResultLauncher
import javax.inject.Inject

@HiltViewModel
internal class ResultListViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
) : BaseViewModel() {

    private val launcher: ResultLauncher = savedStateHandle.getLauncher()
}