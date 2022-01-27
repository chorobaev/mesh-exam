package io.flaterlab.meshexam.presentation.exams

import dagger.hilt.android.lifecycle.HiltViewModel
import io.flaterlab.meshexam.androidbase.BaseViewModel
import io.flaterlab.meshexam.androidbase.SingleLiveEvent
import javax.inject.Inject

@HiltViewModel
internal class ExamsViewModel @Inject constructor(

) : BaseViewModel() {

    val openCreateAction = SingleLiveEvent<Unit>()

    fun onCreatePressed() {
        openCreateAction.call()
    }
}