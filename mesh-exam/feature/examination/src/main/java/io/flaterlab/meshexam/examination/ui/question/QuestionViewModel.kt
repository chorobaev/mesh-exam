package io.flaterlab.meshexam.examination.ui.question

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import dagger.hilt.android.lifecycle.HiltViewModel
import io.flaterlab.meshexam.androidbase.BaseViewModel
import io.flaterlab.meshexam.androidbase.getLauncher
import io.flaterlab.meshexam.examination.dvo.AnswerDvo
import javax.inject.Inject

@HiltViewModel
internal class QuestionViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
) : BaseViewModel() {

    private val launcher: QuestionLauncher = savedStateHandle.getLauncher()

    val question = MutableLiveData<String>()
    val answers = MutableLiveData<List<AnswerDvo>>(emptyList())

    init {
        // TODO: add actual implementation
        question.value = "Who is the current president of the USA?"
        answers.value = (1..4).map {
            AnswerDvo(it.toString(), "Answer #$it", false)
        }
    }

    fun onAnswerClicked(dvo: AnswerDvo) {
        answers.value = answers.value?.map {
            it.copy(isSelected = dvo.id == it.id)
        }
    }
}