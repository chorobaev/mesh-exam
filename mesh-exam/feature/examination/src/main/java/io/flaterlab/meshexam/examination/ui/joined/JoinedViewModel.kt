package io.flaterlab.meshexam.examination.ui.joined

import androidx.lifecycle.MutableLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import io.flaterlab.meshexam.androidbase.BaseViewModel
import io.flaterlab.meshexam.androidbase.SingleLiveEvent
import javax.inject.Inject

@HiltViewModel
internal class JoinedViewModel @Inject constructor(

) : BaseViewModel() {

    val examName = MutableLiveData("History")

    val commandExamStarted = SingleLiveEvent<String>()

    init {
        // TODO: add actual implementation
//        commandExamStarted.value = "examId"
    }
}