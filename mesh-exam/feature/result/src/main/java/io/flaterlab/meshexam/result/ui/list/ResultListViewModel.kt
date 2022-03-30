package io.flaterlab.meshexam.result.ui.list

import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import io.flaterlab.meshexam.androidbase.BaseViewModel
import io.flaterlab.meshexam.androidbase.SingleLiveEvent
import io.flaterlab.meshexam.androidbase.getLauncher
import io.flaterlab.meshexam.androidbase.text.Text
import io.flaterlab.meshexam.core.DateUtils
import io.flaterlab.meshexam.domain.interactor.ProfileInteractor
import io.flaterlab.meshexam.result.ResultLauncher
import io.flaterlab.meshexam.result.dvo.HostExamInfoDvo
import io.flaterlab.meshexam.result.dvo.ResultItemDvo
import io.flaterlab.meshexam.uikit.view.StateRecyclerView
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
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

    val examInfo = profileInteractor.hostingResultMeta(launcher.id)
        .map { metaModel ->
            HostExamInfoDvo(
                examName = metaModel.examName,
                info = metaModel.examInfo,
                duration = DateUtils.formatTimeMmSs(metaModel.durationInMillis),
                submissionCount = metaModel.submissionCount,
                expectedSubmissionCount = metaModel.expectedSubmissionCount,
            )
        }
        .catch { e -> e.showLocalizedMessage() }
        .asLiveData(viewModelScope.coroutineContext)

    val resultListState = MutableLiveData(StateRecyclerView.State.NORMAL)

    private val _resultList = MutableLiveData<List<ResultItemDvo>>()
    val resultList: LiveData<List<ResultItemDvo>> = _resultList

    val commandOpenResult = SingleLiveEvent<String>()

    private var searchText: String? = null
    private var backingResultList: List<ResultItemDvo> = emptyList()

    init {
        profileInteractor.hostingResults(launcher.id)
            .map { list ->
                list.map { model ->
                    ResultItemDvo(
                        id = model.id,
                        fullName = model.studentFullName,
                        info = Text.from(model.studentInfo),
                        status = Text.from(
                            model.status.toString()
                                .lowercase()
                                .replaceFirstChar { it.uppercase() }
                        ),
                        grade = model.grade.roundToInt(),
                        totalGrade = model.totalGrade,
                    )
                }
            }
            .onEach { list ->
                backingResultList = list
                updateResultList()
            }
            .catch { e -> e.showLocalizedMessage() }
            .launchIn(viewModelScope)
    }

    fun onResultClicked(dvo: ResultItemDvo) {
        commandOpenResult.value = dvo.id
    }

    fun onSearchTextChanged(text: String?) {
        searchText = text
        updateResultList()
    }

    private fun updateResultList() {
        val list = searchText.takeIf { !it.isNullOrBlank() }
            ?.let { text ->
                backingResultList
                    .filter { it.fullName.contains(text, ignoreCase = true) }
            } ?: backingResultList

        resultListState.value = when {
            list.isEmpty() -> StateRecyclerView.State.EMPTY
            else -> StateRecyclerView.State.NORMAL
        }
        _resultList.value = list
    }
}