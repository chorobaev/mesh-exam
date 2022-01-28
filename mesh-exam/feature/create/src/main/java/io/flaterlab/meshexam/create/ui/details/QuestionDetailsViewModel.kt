package io.flaterlab.meshexam.create.ui.details

import androidx.lifecycle.SavedStateHandle
import dagger.hilt.android.lifecycle.HiltViewModel
import io.flaterlab.meshexam.androidbase.BaseViewModel
import io.flaterlab.meshexam.androidbase.getLauncher
import javax.inject.Inject

@HiltViewModel
class QuestionDetailsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
) : BaseViewModel() {

    val launcher: QuestionDetailsLauncher = savedStateHandle.getLauncher()
}