package io.flaterlab.meshexam.result.ui.list

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import dagger.hilt.android.lifecycle.HiltViewModel
import io.flaterlab.meshexam.androidbase.BaseViewModel
import io.flaterlab.meshexam.androidbase.SingleLiveEvent
import io.flaterlab.meshexam.androidbase.getLauncher
import io.flaterlab.meshexam.androidbase.text.Text
import io.flaterlab.meshexam.result.ResultLauncher
import io.flaterlab.meshexam.result.dvo.HostExamInfoDvo
import io.flaterlab.meshexam.result.dvo.ResultItemDvo
import io.flaterlab.meshexam.uikit.view.StateRecyclerView
import javax.inject.Inject

@HiltViewModel
internal class ResultListViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
) : BaseViewModel() {

    private val launcher: ResultLauncher = savedStateHandle.getLauncher()

    val examInfo = MutableLiveData<HostExamInfoDvo>()
    val resultListState = MutableLiveData(StateRecyclerView.State.NORMAL)
    val resultList = MutableLiveData<List<ResultItemDvo>>()

    val commandOpenResult = SingleLiveEvent<String>()

    init {
        // TODO: add actual implementation
        examInfo.value = HostExamInfoDvo(
            "History of Kyrgyzstan",
            "State exam",
            "30:00",
            27,
            30
        )

        resultList.value = (1..10).map {
            ResultItemDvo(
                it.toString(),
                "${listOf("Van Dam", "Jeki Chan").random()} #$it",
                info = Text.from("COM-18"),
                status = Text.from("attempting"),
                grade = 80,
                totalGrade = 100,
            )
        }
    }

    fun onResultClicked(dvo: ResultItemDvo) {
        commandOpenResult.value = dvo.id
    }

    fun onSearchTextChanged(text: String?) {
        // TODO: implement a search logic
    }
}