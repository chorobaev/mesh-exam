package io.flaterlab.meshexam.presentation.exams

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.flaterlab.meshexam.androidbase.BaseViewModel
import io.flaterlab.meshexam.androidbase.SingleLiveEvent
import io.flaterlab.meshexam.domain.api.usecase.GetMyExamUseCase
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
internal class ExamsViewModel @Inject constructor(
    private val getMyExamsUseCase: GetMyExamUseCase,
) : BaseViewModel() {

    val openCreateAction = SingleLiveEvent<Unit>()

    fun onCreatePressed() {
        openCreateAction.call()
    }
}