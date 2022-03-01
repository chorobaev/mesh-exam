package io.flaterlab.meshexam.feature.meshroom.ui.list

import androidx.lifecycle.MutableLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import io.flaterlab.meshexam.androidbase.BaseViewModel
import io.flaterlab.meshexam.androidbase.text.Text
import io.flaterlab.meshexam.feature.meshroom.R
import io.flaterlab.meshexam.feature.meshroom.dvo.StudentDvo
import io.flaterlab.meshexam.uikit.view.StateRecyclerView
import javax.inject.Inject

@HiltViewModel
internal class StudentListMonitorViewModel @Inject constructor(

) : BaseViewModel() {

    val studentList = MutableLiveData<List<StudentDvo>>()
    val studentListState = MutableLiveData(StateRecyclerView.State.NORMAL)

    init {
        // TODO: add actual implementation
        studentList.value = (1..10).map {
            StudentDvo(
                id = it.toString(),
                fullName = "${listOf("Alan Turing", "Jan Hog", "Someone Else").random()} #$it",
                info = Text.from("COM-18"),
                status = Text.from("attempting"),
                statusColor = R.color.red_500
            )
        }
    }

    fun onSearchTextChanged(text: String?) {

    }
}