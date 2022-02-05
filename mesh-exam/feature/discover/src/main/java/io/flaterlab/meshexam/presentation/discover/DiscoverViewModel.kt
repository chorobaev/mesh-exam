package io.flaterlab.meshexam.presentation.discover

import dagger.hilt.android.lifecycle.HiltViewModel
import io.flaterlab.meshexam.androidbase.BaseViewModel
import io.flaterlab.meshexam.androidbase.text.Text
import io.flaterlab.meshexam.domain.create.usecase.DiscoverExamsUseCase
import io.flaterlab.meshexam.presentation.discover.dvo.AvailableExamDvo
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class DiscoverViewModel @Inject constructor(
    discoverExamsUseCase: DiscoverExamsUseCase,
) : BaseViewModel() {

    val exams = discoverExamsUseCase()
        .map { list ->
            list.map { exam ->
                AvailableExamDvo(exam.id, exam.name, exam.host, exam.duration)
            }
        }
        .catch { e ->
            e.localizedMessage?.let(Text::from)?.let(message::setValue)
        }

    fun onExamClicked(dvo: AvailableExamDvo) {

    }
}