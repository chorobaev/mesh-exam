package io.flaterlab.meshexam.presentation.discover.ui.main

import androidx.lifecycle.MutableLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import io.flaterlab.meshexam.androidbase.BaseViewModel
import io.flaterlab.meshexam.androidbase.SingleLiveEvent
import io.flaterlab.meshexam.domain.interactor.ExaminationInteractor
import io.flaterlab.meshexam.presentation.discover.dvo.AvailableExamDvo
import io.flaterlab.meshexam.uikit.view.StateRecyclerView
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class DiscoverViewModel @Inject constructor(
    examinationInteractor: ExaminationInteractor,
) : BaseViewModel() {

    val exams: Flow<List<AvailableExamDvo>> = examinationInteractor.discoverExams()
        .map { list ->
            list.map { exam ->
                AvailableExamDvo(exam.id, exam.name, exam.host, exam.duration)
            }
        }
        .onEach { list ->
            examListState.value = when {
                list.isEmpty() -> StateRecyclerView.State.EMPTY
                else -> StateRecyclerView.State.NORMAL
            }
        }
        .catch { it.showLocalizedMessage() }
    val examListState = MutableLiveData(StateRecyclerView.State.LOADING)

    val commandOpenExam = SingleLiveEvent<AvailableExamDvo>()

    fun onExamClicked(dvo: AvailableExamDvo) {
        commandOpenExam.value = dvo
    }
}