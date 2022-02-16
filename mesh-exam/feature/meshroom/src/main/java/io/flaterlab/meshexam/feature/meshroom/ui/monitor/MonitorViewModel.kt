package io.flaterlab.meshexam.feature.meshroom.ui.monitor

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.flaterlab.meshexam.androidbase.BaseViewModel
import io.flaterlab.meshexam.androidbase.getLauncher
import io.flaterlab.meshexam.domain.create.usecase.GetExamUseCase
import io.flaterlab.meshexam.feature.meshroom.dvo.ExamInfoDvo
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class MonitorViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getExamUseCase: GetExamUseCase,
) : BaseViewModel() {

    private val launcher: MonitorLauncher = savedStateHandle.getLauncher()

    val exam = MutableLiveData<ExamInfoDvo>()

    init {
        loadExam()
    }

    private fun loadExam() {
        viewModelScope.launch {
            val info = getExamUseCase(launcher.examId).exam
            exam.value = ExamInfoDvo(info.id, info.name, info.type)
        }
    }
}