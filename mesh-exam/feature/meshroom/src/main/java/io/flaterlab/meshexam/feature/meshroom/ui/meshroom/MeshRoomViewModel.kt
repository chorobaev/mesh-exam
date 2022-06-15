package io.flaterlab.meshexam.feature.meshroom.ui.meshroom

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.flaterlab.meshexam.androidbase.BaseViewModel
import io.flaterlab.meshexam.androidbase.SingleLiveEvent
import io.flaterlab.meshexam.androidbase.getLauncher
import io.flaterlab.meshexam.androidbase.text.Text
import io.flaterlab.meshexam.domain.create.usecase.GetExamUseCase
import io.flaterlab.meshexam.domain.interactor.MeshInteractor
import io.flaterlab.meshexam.feature.meshroom.R
import io.flaterlab.meshexam.feature.meshroom.dvo.ClientDvo
import io.flaterlab.meshexam.feature.meshroom.dvo.ExamInfoDvo
import io.flaterlab.meshexam.feature.meshroom.ui.monitor.MonitorLauncher
import io.flaterlab.meshexam.uikit.view.StateRecyclerView
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.absoluteValue

@HiltViewModel
internal class MeshRoomViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getExamUseCase: GetExamUseCase,
    private val meshInteractor: MeshInteractor,
) : BaseViewModel() {

    private val launcher = savedStateHandle.getLauncher<MeshRoomLauncher>()

    val studentAmount = MutableLiveData(0)
    val exam = getExamUseCase(launcher.examId)
        .map { it.exam }
        .map { info ->
            ExamInfoDvo(info.id, info.name, info.type)
        }
        .asLiveData(viewModelScope.coroutineContext)
    val clients = MutableLiveData<List<ClientDvo>>()
    val clientsListState = MutableLiveData(StateRecyclerView.State.EMPTY)

    val commandStartExam = SingleLiveEvent<MonitorLauncher>()
    val commandShowLeavePrompt = SingleLiveEvent<Unit>()
    val commandLeaveMeshroom = SingleLiveEvent<Unit>()


    private var _clients: List<ClientDvo> = emptyList()
    private var searchText: String? = null
    private var meshJob: Job? = null

    init {
        startMesh()
    }

    private fun startMesh() {
        meshJob?.cancel()
        meshJob = meshInteractor.creteMesh(launcher.examId)
            .onEach { meshModel ->
                _clients = meshModel.clients.map { client ->
                    ClientDvo(
                        client.id,
                        client.fullName,
                        Text.from(client.info),
                        Text.from(
                            if (client.positionInMesh > 0) {
                                R.string.mesh_client_status_intPositionRight
                            } else {
                                R.string.mesh_client_status_intPositionLeft
                            },
                            client.positionInMesh.absoluteValue
                        )
                    )
                }
                updateClientList()
                studentAmount.value = clients.value?.size ?: 0
            }
            .catch { it.localizedMessage?.let(Text::from)?.also(message::setValue) }
            .launchIn(viewModelScope)
    }

    private fun updateClientList() {
        val newClientList = searchText?.let { text ->
            _clients.filter { it.fullName.contains(text) }
        } ?: _clients
        clientsListState.value = if (newClientList.isEmpty()) {
            StateRecyclerView.State.EMPTY
        } else {
            StateRecyclerView.State.NORMAL
        }
        clients.value = newClientList
    }

    fun onClientClicked(dvo: ClientDvo) {
        viewModelScope.launch {
            meshInteractor.removeClient(dvo.id)
        }
    }

    fun onSearchTextChanged(text: String?) {
        searchText = text
        updateClientList()
    }

    fun onBackPressed() {
        commandShowLeavePrompt.call()
    }

    fun onLeaveClicked() {
        viewModelScope.launch {
            try {
                meshInteractor.destroyMesh(launcher.examId)
                commandLeaveMeshroom.call()
            } catch (ex: Exception) {
                ex.showLocalizedMessage()
            }
        }
    }

    fun onStartClicked() {
        viewModelScope.launch {
            try {
                val result = meshInteractor.startExam(launcher.examId)
                commandStartExam.value = MonitorLauncher(result.examId, result.hostingId)
            } catch (ex: Exception) {
                ex.showLocalizedMessage()
            }
        }
    }
}