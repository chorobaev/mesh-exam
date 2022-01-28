package io.flaterlab.meshexam.create.ui.question

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import io.flaterlab.meshexam.androidbase.BaseViewModel
import io.flaterlab.meshexam.androidbase.SingleLiveEvent
import io.flaterlab.meshexam.create.dvo.QuestionMetaDvo
import java.util.*
import javax.inject.Inject

@HiltViewModel
class CreateQuestionViewModel @Inject constructor(

) : BaseViewModel() {

    private val _questionMetaInfo = MutableLiveData<QuestionMetaDvo>()
    val questionMetaInfo: LiveData<QuestionMetaDvo> = _questionMetaInfo

    private val _questionIds = MutableLiveData<List<String>>(emptyList())
    val questionIds: LiveData<List<String>> = _questionIds

    val questionIdAdded = SingleLiveEvent<String>()

    init {
        // TODO: add call to use case
        _questionMetaInfo.value = QuestionMetaDvo(
            "History of Kyrgyzstan",
            "State exam",
            30
        )
        _questionIds.value = (1..3).map {
            UUID.randomUUID().toString()
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