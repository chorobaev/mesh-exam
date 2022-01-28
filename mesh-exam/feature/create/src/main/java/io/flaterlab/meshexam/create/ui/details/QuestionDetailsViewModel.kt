package io.flaterlab.meshexam.create.ui.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import dagger.hilt.android.lifecycle.HiltViewModel
import io.flaterlab.meshexam.androidbase.BaseViewModel
import io.flaterlab.meshexam.androidbase.SingleLiveEvent
import io.flaterlab.meshexam.androidbase.getLauncher
import io.flaterlab.meshexam.create.dvo.AnswerDvo
import java.util.*
import javax.inject.Inject

@HiltViewModel
class QuestionDetailsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
) : BaseViewModel() {

    val launcher: QuestionDetailsLauncher = savedStateHandle.getLauncher()

    private val _question = MutableLiveData("")
    val question: LiveData<String> = _question

    private val _answers = MutableLiveData<List<AnswerDvo>>()
    val answers: LiveData<List<AnswerDvo>> = _answers

    val deleteAnswerCommand = SingleLiveEvent<AnswerDvo>()
    val changeQuestionCommand = SingleLiveEvent<String>()
    val changeAnswerCommand = SingleLiveEvent<AnswerDvo>()

    init {
        _answers.value = (1..5).map {
            AnswerDvo(
                UUID.randomUUID().toString(),
                "Answer #$it",
                false
            )
        }
    }

    fun onQuestionChanged(question: String) {
        _question.value = question
    }

    fun onAnswerChanged(answerId: String, answerContent: String) {
        _answers.value = answers.value?.toMutableList()?.let { list ->
            val answer = list.find { it.id == answerId }?.copy(content = answerContent)
                ?: return@let emptyList()
            mutableListOf<AnswerDvo>().apply {
                list.forEach { dvo ->
                    add(if (dvo.id == answerId) answer else dvo)
                }
            }
        }
    }

    fun onChangeQuestionClicked() {
        changeQuestionCommand.value = question.value
    }

    fun onChangeAnswerTextClicked(dvo: AnswerDvo) {
        changeAnswerCommand.value = dvo
    }

    fun onChangeAnswerCorrectnessClicked(dvo: AnswerDvo, isCorrect: Boolean) {

    }

    fun onAnswerLongClicked(dvo: AnswerDvo) {
        deleteAnswerCommand.value = dvo
    }
}