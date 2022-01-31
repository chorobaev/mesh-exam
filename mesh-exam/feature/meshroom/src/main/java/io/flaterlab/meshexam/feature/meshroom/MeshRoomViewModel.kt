package io.flaterlab.meshexam.feature.meshroom

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.flaterlab.meshexam.androidbase.BaseViewModel
import io.flaterlab.meshexam.androidbase.getLauncher
import io.flaterlab.meshexam.androidbase.text.Text
import io.flaterlab.meshexam.domain.create.usecase.GetExamUseCase
import io.flaterlab.meshexam.domain.mesh.usecase.CreateMeshUseCase
import io.flaterlab.meshexam.domain.mesh.usecase.RemoveClientUseCase
import io.flaterlab.meshexam.feature.meshroom.dvo.ClientDvo
import io.flaterlab.meshexam.feature.meshroom.dvo.ExamInfoDvo
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class MeshRoomViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getExamUseCase: GetExamUseCase,
    private val createMeshUseCase: CreateMeshUseCase,
    private val removeClientUseCase: RemoveClientUseCase,
) : BaseViewModel() {

    private val launcher = savedStateHandle.getLauncher<MeshRoomLauncher>()

    val studentAmount = MutableLiveData(0)
    val exam = MutableLiveData<ExamInfoDvo>()
    val clients = MutableLiveData<List<ClientDvo>>(emptyList())

    private var _clients: List<ClientDvo> = emptyList()
    private var searchText: String? = null
    private var meshJob: Job? = null

    init {
        loadExam()
        startMesh()
    }

    private fun loadExam() {
        viewModelScope.launch {
            val info = getExamUseCase(launcher.examId).exam
            exam.value = ExamInfoDvo(info.id, info.name)
        }
    }

    private fun startMesh() {
        meshJob?.cancel()
        meshJob = createMeshUseCase(launcher.examId)
            .onEach { meshModel ->
                _clients = meshModel.clients.map { client ->
                    ClientDvo(client.id, client.fullName, client.info, client.status)
                }
                updateClientList()
                studentAmount.value = clients.value?.size ?: 0
            }
            .catch { it.localizedMessage?.let(Text::from)?.also(message::setValue) }
            .launchIn(viewModelScope)
    }

    private fun updateClientList() {
        clients.value = searchText?.let { text ->
            _clients.filter { it.fullName.contains(text) }
        } ?: _clients
    }

    fun onClientClicked(dvo: ClientDvo) {
        viewModelScope.launch {
            removeClientUseCase(dvo.id)
        }
    }

    fun onSearchTextChanged(text: String?) {
        searchText = text
        updateClientList()
    }
}