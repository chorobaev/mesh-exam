package io.flaterlab.meshexam.result.ui.result

import androidx.lifecycle.SavedStateHandle
import dagger.hilt.android.lifecycle.HiltViewModel
import io.flaterlab.meshexam.androidbase.BaseViewModel
import io.flaterlab.meshexam.androidbase.getLauncher
import io.flaterlab.meshexam.result.ClientResultLauncher
import javax.inject.Inject

@HiltViewModel
internal class IndividualResultViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle
) : BaseViewModel() {

    private val launcher: ClientResultLauncher = savedStateHandle.getLauncher()

}