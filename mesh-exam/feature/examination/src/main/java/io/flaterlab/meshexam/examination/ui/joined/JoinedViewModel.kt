package io.flaterlab.meshexam.examination.ui.joined

import androidx.lifecycle.MutableLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import io.flaterlab.meshexam.androidbase.BaseViewModel
import javax.inject.Inject

@HiltViewModel
internal class JoinedViewModel @Inject constructor(

) : BaseViewModel() {

    val examName = MutableLiveData("History")
}