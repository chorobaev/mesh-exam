package io.flaterlab.meshexam.presentation.exams

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.flaterlab.meshexam.androidbase.BaseViewModel
import io.flaterlab.meshexam.androidbase.SingleLiveEvent
import io.flaterlab.meshexam.domain.create.usecase.GetMyExamsUseCase
import io.flaterlab.meshexam.domain.interactor.ExamContentInteractor
import io.flaterlab.meshexam.presentation.exams.dvo.ExamDvo
import io.flaterlab.meshexam.uikit.view.StateRecyclerView
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class ExamsViewModel @Inject constructor(
    private val getMyExamsUseCase: GetMyExamsUseCase,
    private val examContentInteractor: ExamContentInteractor,
) : BaseViewModel() {

    val exams = MutableLiveData<List<ExamDvo>>(emptyList())
    val examListState = MutableLiveData(StateRecyclerView.State.LOADING)

    val openCreateCommand = SingleLiveEvent<Unit>()
    val openExamCommand = SingleLiveEvent<ExamDvo>()
    val confirmExamDeletionCommand = SingleLiveEvent<ExamDvo>()

    init {
        loadExams()
    }

    private fun loadExams() {
        getMyExamsUseCase()
            .map { list ->
                list.map { ExamDvo(it.id, it.name, it.durationInMin) }
            }
            .onEach { exams ->
                examListState.value = when {
                    exams.isEmpty() -> StateRecyclerView.State.EMPTY
                    else -> StateRecyclerView.State.NORMAL
                }
                this.exams.value = exams
            }
            .catch { it.showLocalizedMessage() }
            .launchIn(viewModelScope)
    }

    fun onExamPressed(exam: ExamDvo) {
        openExamCommand.value = exam
    }

    fun onExamLongPressed(exam: ExamDvo) {
        confirmExamDeletionCommand.value = exam
    }

    fun onExamDeletionConfirmed(exam: ExamDvo) {
        viewModelScope.launch {
            try {
                examContentInteractor.deleteExamById(exam.id)
            } catch (e: Exception) {
                e.showLocalizedMessage()
            }
        }
    }

    fun onCreatePressed() {
        openCreateCommand.call()
    }
}