package io.flaterlab.meshexam.feature.meshroom.ui.finishing

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import dagger.hilt.android.lifecycle.HiltViewModel
import io.flaterlab.meshexam.androidbase.BaseViewModel
import io.flaterlab.meshexam.androidbase.SingleLiveEvent
import io.flaterlab.meshexam.androidbase.getLauncher
import io.flaterlab.meshexam.androidbase.text.Text
import io.flaterlab.meshexam.feature.meshroom.R
import io.flaterlab.meshexam.feature.meshroom.dvo.SubmissionDvo
import io.flaterlab.meshexam.feature.meshroom.ui.result.HostResultLauncher
import io.flaterlab.meshexam.uikit.view.StateRecyclerView
import javax.inject.Inject

@HiltViewModel
internal class FinishingViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
) : BaseViewModel() {

    private val launcher: FinishingLauncher = savedStateHandle.getLauncher()

    val submissionAmount = MutableLiveData<Pair<Int, Int>>()
    val submissionList = MutableLiveData<List<SubmissionDvo>>()
    val submissionListStat = MutableLiveData(StateRecyclerView.State.NORMAL)

    val commandConfirmFinish = SingleLiveEvent<Unit>()
    val commandOpenResult = SingleLiveEvent<HostResultLauncher>()

    init {
        // TODO: add actual implementation
        submissionAmount.value = 5 to 30
        submissionList.value = (1..10).map {
            SubmissionDvo(
                id = it.toString(),
                fullName = "${listOf("Alan Turing", "Jan Hog", "Someone Else").random()} #$it",
                info = Text.from("COM-18"),
                status = Text.from("submitted"),
                statusColorResId = R.color.purple_500
            )
        }
    }

    fun onFinishImmediatelyClicked() {
        commandConfirmFinish.call()
    }

    fun onFinishImmediatelyConfirmed() {
        commandOpenResult.value = HostResultLauncher(launcher.attemptId)
    }

    fun onBackPressed() {

    }
}