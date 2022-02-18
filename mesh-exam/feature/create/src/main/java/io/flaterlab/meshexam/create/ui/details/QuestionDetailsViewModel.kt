package io.flaterlab.meshexam.create.ui.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.flaterlab.meshexam.androidbase.BaseViewModel
import io.flaterlab.meshexam.androidbase.SingleLiveEvent
import io.flaterlab.meshexam.androidbase.getLauncher
import io.flaterlab.meshexam.androidbase.text.Text
import io.flaterlab.meshexam.create.R
import io.flaterlab.meshexam.create.dvo.AnswerDvo
import io.flaterlab.meshexam.create.dvo.QuestionDvo
import io.flaterlab.meshexam.domain.create.model.CreateAnswerModel
import io.flaterlab.meshexam.domain.create.usecase.*
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class QuestionDetailsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    getQuestionUseCase: GetQuestionUseCase,
    getAnswersUseCase: GetAnswersUseCase,
    private val updateQuestionContentUseCase: UpdateQuestionContentUseCase,
    private val updateAnswerContentUseCase: UpdateAnswerContentUseCase,
    private val createAnswerUseCase: CreateAnswerUseCase,
    private val deleteAnswerUseCase: DeleteAnswerUseCase,
    private val updateAnswerCorrectnessUseCase: UpdateAnswerCorrectnessUseCase,
) : BaseViewModel() {

    private val launcher: QuestionDetailsLauncher = savedStateHandle.getLauncher()

    val question = getQuestionUseCase(launcher.questionId)
        .map { QuestionDvo(it.id, it.content, it.type.toString(), it.score) }
        .onEach(::_question::set)
        .onEach { Timber.d(it.toString()) }
        .catch { }

    val answers = getAnswersUseCase(launcher.questionId)
        .map { list ->
            list.map { model ->
                AnswerDvo(model.id, model.content, model.isCorrect)
            }
        }
        .onEach(::_answers::set)
        .onEach { Timber.d(it.toString()) }
        .catch { it.localizedMessage?.let(Text::from)?.also(message::setValue) }

    val changeQuestionCommand = SingleLiveEvent<QuestionDvo>()
    val changeAnswerCommand = SingleLiveEvent<AnswerDvo>()

    private var _question: QuestionDvo? = null
    private var _answers: List<AnswerDvo> = emptyList()

    fun onQuestionChanged(question: String) {
        viewModelScope.launch {
            updateQuestionContentUseCase(launcher.questionId, question)
        }
    }

    fun onAnswerChanged(answerId: String, answerContent: String) {
        viewModelScope.launch {
            updateAnswerContentUseCase(answerId, answerContent)
        }
    }

    fun onChangeQuestionClicked() {
        _question?.let(changeQuestionCommand::setValue)
    }

    fun onChangeAnswerTextClicked(dvo: AnswerDvo) {
        changeAnswerCommand.value = dvo
    }

    fun onChangeAnswerCorrectnessClicked(dvo: AnswerDvo, isCorrect: Boolean) {
        viewModelScope.launch {
            updateAnswerCorrectnessUseCase(dvo.id, isCorrect)
        }
    }

    fun onAnswerLongClicked(dvo: AnswerDvo) {
        val position = _answers.indexOf(dvo)
        val variant = ('a' + position).toString()
        viewModelScope.launch {
            deleteAnswerUseCase(dvo.id)
            message.value = Text.from(R.string.create_create_question_answerStringDeleted, variant)
        }
    }

    fun onAddAnswerClicked() {
        viewModelScope.launch {
            createAnswerUseCase(
                CreateAnswerModel(launcher.questionId, _answers.size)
            )
        }
    }
}