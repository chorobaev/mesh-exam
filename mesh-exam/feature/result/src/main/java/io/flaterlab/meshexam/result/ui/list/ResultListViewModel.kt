package io.flaterlab.meshexam.result.ui.list

import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import io.flaterlab.meshexam.androidbase.BaseViewModel
import io.flaterlab.meshexam.androidbase.SingleLiveEvent
import io.flaterlab.meshexam.androidbase.getLauncher
import io.flaterlab.meshexam.androidbase.text.Text
import io.flaterlab.meshexam.domain.interactor.ProfileInteractor
import io.flaterlab.meshexam.result.ResultLauncher
import io.flaterlab.meshexam.result.dvo.HostExamInfoDvo
import io.flaterlab.meshexam.result.dvo.ResultItemDvo
import io.flaterlab.meshexam.uikit.view.StateRecyclerView
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject
import kotlin.math.roundToInt

@HiltViewModel
internal class ResultListViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    profileInteractor: ProfileInteractor,
) : BaseViewModel() {

    private val launcher: ResultLauncher = savedStateHandle.getLauncher()

    val examInfo = MutableLiveData<HostExamInfoDvo>()
    val resultListState = MutableLiveData(StateRecyclerView.State.NORMAL)
    val resultList: LiveData<List<ResultItemDvo>> =
        profileInteractor.hostingResults(launcher.id)
            .map { list ->
                list.map { model ->
                    ResultItemDvo(
                        id = model.id,
                        fullName = model.studentFullName,
                        info = Text.from(model.studentInfo),
                        status = Text.from(model.status.toString()),
                        grade = model.grade.roundToInt(),
                        totalGrade = model.totalGrade,
                    )
                }
            }
            .onEach { list ->
                resultListState.value = when {
                    list.isEmpty() -> StateRecyclerView.State.EMPTY
                    else -> StateRecyclerView.State.NORMAL
                }
            }
            .asLiveData(viewModelScope.coroutineContext)

    val commandOpenResult = SingleLiveEvent<String>()

    init {
        // TODO: add actual implementation
        examInfo.value = HostExamInfoDvo(
            "History of Kyrgyzstan",
            "State exam",
            "30:00",
            27,
            30
        )
    }

    fun onResultClicked(dvo: ResultItemDvo) {
        commandOpenResult.value = dvo.id
    }

    fun onSearchTextChanged(text: String?) {
        // TODO: implement a search logic
    }
}