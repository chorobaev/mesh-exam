package io.flaterlab.meshexam.examination.ui.exam

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.flaterlab.meshexam.androidbase.BaseViewModel
import io.flaterlab.meshexam.examination.dvo.ExaminationDvo
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@HiltViewModel
internal class ExamViewModel @Inject constructor(

) : BaseViewModel() {

    val examMeta = MutableLiveData<ExaminationDvo>()
    val timeLeft = MutableLiveData<String>()
    val questionIds = MutableLiveData<List<String>>(emptyList())

    init {
        // TODO: add actual implementation
        examMeta.value = ExaminationDvo(
            "Human and computer interactions",
            "Final exam"
        )
        viewModelScope.launch {
            var sec = 1800L
            while (isActive && sec > 0) {
                delay(1000)
                timeLeft.value =
                    SimpleDateFormat("mm:ss", Locale.getDefault()).format(Date(sec * 1000))
                sec--
            }
        }
        questionIds.value = (1..5).map {
            "Question $it"
        }
    }

    fun onSubmitClicked() {

    }
}