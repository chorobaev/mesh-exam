package io.flaterlab.meshexam.result.ui.result

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import dagger.hilt.android.lifecycle.HiltViewModel
import io.flaterlab.meshexam.androidbase.BaseViewModel
import io.flaterlab.meshexam.androidbase.getLauncher
import io.flaterlab.meshexam.androidbase.text.Text
import io.flaterlab.meshexam.result.ClientResultLauncher
import io.flaterlab.meshexam.result.dvo.IndividualExamInfoDvo
import io.flaterlab.meshexam.result.dvo.ResultQuestionInfoDvo
import javax.inject.Inject

@HiltViewModel
internal class IndividualResultViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle
) : BaseViewModel() {

    private val launcher: ClientResultLauncher = savedStateHandle.getLauncher()

    val metaInfo = MutableLiveData<IndividualExamInfoDvo>()
    val questionIdList = MutableLiveData<List<ResultQuestionInfoDvo>>()

    init {
        // TODO: add actual implementation
        metaInfo.value = IndividualExamInfoDvo(
            "lklkj",
            name = "History",
            "State exam",
            listOf(
                Text.from("Time:") to Text.from("10:00:00"),
                Text.from("Total questions:") to Text.from("15"),
            )
        )
        questionIdList.value = (1..5).map {
            ResultQuestionInfoDvo(
                it.toString(),
                listOf(true, false).random()
            )
        }
    }
}