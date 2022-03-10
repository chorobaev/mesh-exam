package io.flaterlab.meshexam.result.ui.question

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import dagger.hilt.android.lifecycle.HiltViewModel
import io.flaterlab.meshexam.androidbase.BaseViewModel
import io.flaterlab.meshexam.androidbase.getLauncher
import io.flaterlab.meshexam.result.dvo.ResultAnswerDvo
import io.flaterlab.meshexam.result.dvo.ResultQuestionDvo
import java.util.*
import javax.inject.Inject

@HiltViewModel
internal class ResultQuestionViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
) : BaseViewModel() {

    private val launcher: ResultQuestionLauncher = savedStateHandle.getLauncher()

    val question = MutableLiveData<ResultQuestionDvo>()

    init {
        // TODO: Add actual implementation
        val correctIndex = (1..3).random()
        val selectedIndex = (1..3).random()
        question.value = ResultQuestionDvo(
            UUID.randomUUID().toString(),
            "What is the mass of Mars?",
            (1..15).map {
                ResultAnswerDvo(
                    UUID.randomUUID().toString(),
                    "Answer ${'a' + it}",
                    it == correctIndex,
                    it == selectedIndex
                )
            }
        )
    }
}