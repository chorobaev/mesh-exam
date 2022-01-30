package io.flaterlab.meshexam.presentation.exams

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.flaterlab.meshexam.androidbase.BaseViewModel
import io.flaterlab.meshexam.androidbase.SingleLiveEvent
import io.flaterlab.meshexam.androidbase.text.Text
import io.flaterlab.meshexam.domain.create.usecase.GetMyExamsUseCase
import io.flaterlab.meshexam.presentation.exams.dvo.ExamDvo
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
internal class ExamsViewModel @Inject constructor(
    private val getMyExamsUseCase: GetMyExamsUseCase,
) : BaseViewModel() {

    val exams = MutableLiveData<List<ExamDvo>>(emptyList())

    val openCreateCommand = SingleLiveEvent<Unit>()
    val openExamCommand = SingleLiveEvent<ExamDvo>()

    init {
        loadExams()
    }

    fun loadExams() {
        getMyExamsUseCase()
            .map { list ->
                list.map { ExamDvo(it.id, it.name, it.durationInMin) }
            }
            .onEach(exams::setValue)
            .catch { it.localizedMessage?.let(Text::from)?.let(message::setValue) }
            .launchIn(viewModelScope)
    }

    fun onExamPressed(exam: ExamDvo) {
        openExamCommand.value = exam
    }

    fun onCreatePressed() {
        openCreateCommand.call()
    }
}