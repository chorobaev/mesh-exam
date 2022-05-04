package io.flaterlab.meshexam.feature.meshroom.ui.list

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.flaterlab.meshexam.androidbase.BaseViewModel
import io.flaterlab.meshexam.androidbase.getLauncher
import io.flaterlab.meshexam.androidbase.text.Text
import io.flaterlab.meshexam.domain.interactor.MeshInteractor
import io.flaterlab.meshexam.domain.mesh.model.HostedStudentModel
import io.flaterlab.meshexam.feature.meshroom.R
import io.flaterlab.meshexam.feature.meshroom.dvo.StudentDvo
import io.flaterlab.meshexam.uikit.view.StateRecyclerView
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
internal class StudentListMonitorViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val meshInteractor: MeshInteractor,
) : BaseViewModel() {

    private val launcher: StudentListLauncher = savedStateHandle.getLauncher()

    private val searchText = MutableStateFlow<String?>(null)

    @OptIn(ExperimentalCoroutinesApi::class)
    val studentList = searchText
        .flatMapLatest { text ->
            meshInteractor.hostedStudentList(launcher.hostingId, text)
        }
        .map { list ->
            list.map { studentModel ->
                StudentDvo(
                    id = studentModel.userId,
                    fullName = studentModel.fullName,
                    info = Text.from(studentModel.info),
                    status = Text.from(
                        when (studentModel.status) {
                            HostedStudentModel.Status.ATTEMPTING ->
                                R.string.monitor_studentList_statusAttempting
                            HostedStudentModel.Status.SUBMITTED ->
                                R.string.monitor_studentList_statusSubmitted
                            HostedStudentModel.Status.DISCONNECTED ->
                                R.string.monitor_studentList_statusDisconnected
                        }
                    ),
                    statusColor = when (studentModel.status) {
                        HostedStudentModel.Status.ATTEMPTING -> R.color.purple_500
                        HostedStudentModel.Status.SUBMITTED -> R.color.gray_2
                        HostedStudentModel.Status.DISCONNECTED -> R.color.red_800
                    }
                )
            }
        }
        .onEach { list ->
            studentListState.value = if (list.isEmpty()) {
                StateRecyclerView.State.EMPTY
            } else {
                StateRecyclerView.State.NORMAL
            }
        }
        .catch { e -> e.showLocalizedMessage() }
        .asLiveData(viewModelScope.coroutineContext)
    val studentListState = MutableLiveData(StateRecyclerView.State.NORMAL)

    fun onSearchTextChanged(text: String?) {
        searchText.value = text
    }
}