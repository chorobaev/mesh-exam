package io.flaterlab.meshexam.create.ui.exam

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.flaterlab.meshexam.androidbase.BaseViewModel
import io.flaterlab.meshexam.androidbase.SingleLiveEvent
import io.flaterlab.meshexam.androidbase.text.Text
import io.flaterlab.meshexam.create.R
import io.flaterlab.meshexam.domain.create.model.CreateExamModel
import io.flaterlab.meshexam.domain.create.usecase.CreateExamUseCase
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class CreateExamViewModel @Inject constructor(
    private val createExamUseCase: CreateExamUseCase
) : BaseViewModel() {

    val nameError = MutableLiveData(Text.empty())
    val typeError = MutableLiveData(Text.empty())
    val durationError = MutableLiveData(Text.empty())
    val nextEnabled = MutableLiveData(false)
    val openQuestionScreenCommand = SingleLiveEvent<String>()

    private var name: String? = null
    private var type: String? = null
    private var duration: String? = null

    fun onNameChanged(name: String?) {
        this.name = name
        nameError.value = if (!isNameValid()) {
            Text.from(R.string.create_create_exam_fillExamName)
        } else {
            Text.empty()
        }
        onFieldChanged()
    }

    private fun isNameValid(): Boolean {
        return !name.isNullOrBlank()
    }

    fun onTypeChanged(type: String?) {
        this.type = type
        onFieldChanged()
    }

    private fun isTypeValid(): Boolean {
        return true
    }

    fun onDurationChanged(duration: String?) {
        this.duration = duration
        durationError.value = if (!isDurationValid()) {
            Text.from(R.string.create_create_exam_fillDuration)
        } else {
            Text.empty()
        }
        onFieldChanged()
    }

    private fun isDurationValid(): Boolean {
        return !duration.isNullOrBlank()
    }

    private fun onFieldChanged() {
        nextEnabled.value = isNameValid() && isTypeValid() && isDurationValid()
    }

    fun onNextClicked() {
        viewModelScope.launch {
            val examId: String = createExamUseCase(
                CreateExamModel(name!!, type, duration!!.toInt())
            )
            openQuestionScreenCommand.value = examId
        }
    }
}