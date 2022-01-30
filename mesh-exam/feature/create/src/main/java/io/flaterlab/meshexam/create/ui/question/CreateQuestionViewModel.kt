package io.flaterlab.meshexam.create.ui.question

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.flaterlab.meshexam.androidbase.BaseViewModel
import io.flaterlab.meshexam.androidbase.SingleLiveEvent
import io.flaterlab.meshexam.androidbase.getLauncher
import io.flaterlab.meshexam.androidbase.text.Text
import io.flaterlab.meshexam.create.R
import io.flaterlab.meshexam.create.dvo.QuestionMetaDvo
import io.flaterlab.meshexam.domain.model.CreateQuestionModel
import io.flaterlab.meshexam.domain.usecase.CreateQuestionUseCase
import io.flaterlab.meshexam.domain.usecase.DeleteQuestionUseCase
import io.flaterlab.meshexam.domain.usecase.GetExamUseCase
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class CreateQuestionViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getExamUseCase: GetExamUseCase,
    private val createQuestionUseCase: CreateQuestionUseCase,
    private val deleteQuestionUseCase: DeleteQuestionUseCase,
) : BaseViewModel() {

    private val _questionMetaInfo = MutableLiveData<QuestionMetaDvo>()
    val questionMetaInfo: LiveData<QuestionMetaDvo> = _questionMetaInfo

    private val _questionIds = MutableLiveData<List<String>>(emptyList())
    val questionIds: LiveData<List<String>> = _questionIds

    val questionIdAdded = SingleLiveEvent<String>()

    private val launcher: CreateQuestionLauncher = savedStateHandle.getLauncher()

    init {
        loadExam()
    }

    fun loadExam() {
        viewModelScope.launch {
            getExamUseCase(launcher.examId).apply {
                Timber.d(toString())
                _questionMetaInfo.value = QuestionMetaDvo(exam.name, exam.type, exam.durationInMin)
                _questionIds.value = questionIds
            }
        }
    }

    fun onAddQuestionClicked() {
        viewModelScope.launch {
            val ids = questionIds.value!!
            val newQuestionId = createQuestionUseCase(
                CreateQuestionModel(
                    examId = launcher.examId,
                    orderNumber = ids.size,
                    score = 1F,
                )
            )
            _questionIds.value = ids + newQuestionId
            questionIdAdded.value = newQuestionId
        }
    }

    fun onDeleteQuestionAt(position: Int) {
        viewModelScope.launch {
            val ids = questionIds.value!!
            deleteQuestionUseCase(ids[position])
            _questionIds.value = ids.toMutableList().apply {
                removeAt(position)
            }
            message.value =
                Text.from(R.string.create_create_question_questionAtIntDeleted, position + 1)
        }
    }
}