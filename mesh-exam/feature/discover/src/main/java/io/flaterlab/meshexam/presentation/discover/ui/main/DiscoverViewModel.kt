package io.flaterlab.meshexam.presentation.discover.ui.main

import dagger.hilt.android.lifecycle.HiltViewModel
import io.flaterlab.meshexam.androidbase.BaseViewModel
import io.flaterlab.meshexam.androidbase.SingleLiveEvent
import io.flaterlab.meshexam.domain.create.usecase.DiscoverExamsUseCase
import io.flaterlab.meshexam.presentation.discover.dvo.AvailableExamDvo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class DiscoverViewModel @Inject constructor(
    discoverExamsUseCase: DiscoverExamsUseCase,
) : BaseViewModel() {

    val exams: Flow<List<AvailableExamDvo>> = discoverExamsUseCase()
        .map { list ->
            list.map { exam ->
                AvailableExamDvo(exam.id, exam.name, exam.host, exam.duration)
            }
        }
        .catch { it.showLocalizedMessage() }

    val commandOpenExam = SingleLiveEvent<AvailableExamDvo>()

    fun onExamClicked(dvo: AvailableExamDvo) {
        commandOpenExam.value = dvo
    }
}