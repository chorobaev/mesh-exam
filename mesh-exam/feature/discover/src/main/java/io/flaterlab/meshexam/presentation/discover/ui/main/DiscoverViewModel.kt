package io.flaterlab.meshexam.presentation.discover.ui.main

import dagger.hilt.android.lifecycle.HiltViewModel
import io.flaterlab.meshexam.androidbase.BaseViewModel
import io.flaterlab.meshexam.androidbase.SingleLiveEvent
import io.flaterlab.meshexam.domain.create.usecase.DiscoverExamsUseCase
import io.flaterlab.meshexam.presentation.discover.dvo.AvailableExamDvo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import java.util.*
import javax.inject.Inject

@HiltViewModel
class DiscoverViewModel @Inject constructor(
    discoverExamsUseCase: DiscoverExamsUseCase,
) : BaseViewModel() {

    val exams: Flow<List<AvailableExamDvo>> = flowOf((1..5)).map { range ->
        range.map {
            AvailableExamDvo(
                UUID.randomUUID().toString(),
                "History $it",
                "Talgat agai",
                30
            )
        }
    } // discoverExamsUseCase()
//        .map { list ->
//            list.map { exam ->
//                AvailableExamDvo(exam.id, exam.name, exam.host, exam.duration)
//            }
//        }
//        .catch { it.showLocalizedMessage() }

    val commandOpenExam = SingleLiveEvent<AvailableExamDvo>()

    fun onExamClicked(dvo: AvailableExamDvo) {
        commandOpenExam.value = dvo
    }
}