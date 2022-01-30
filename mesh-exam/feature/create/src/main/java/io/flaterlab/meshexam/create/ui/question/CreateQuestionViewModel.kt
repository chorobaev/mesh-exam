package io.flaterlab.meshexam.create.ui.question

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.flaterlab.meshexam.androidbase.BaseViewModel
import io.flaterlab.meshexam.androidbase.SingleLiveEvent
import io.flaterlab.meshexam.androidbase.getLauncher
import io.flaterlab.meshexam.create.dvo.QuestionMetaDvo
import io.flaterlab.meshexam.domain.usecase.GetExamUseCase
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class CreateQuestionViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getExamUseCase: GetExamUseCase,
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
                _questionMetaInfo.value = QuestionMetaDvo(exam.name, exam.type, exam.durationInMin)
                _questionIds.value = questionIds
            }
        }
    }

    fun onAddQuestionClicked() {
        val ids = _questionIds.value?.toMutableList()?.apply {
            add(UUID.randomUUID().toString())
        } ?: return
        _questionIds.value = ids
        questionIdAdded.value = ids.last()
    }
}