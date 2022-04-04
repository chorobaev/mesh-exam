package io.flaterlab.meshexam.feature.meshroom.ui.result

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.flaterlab.meshexam.androidbase.BaseViewModel
import io.flaterlab.meshexam.androidbase.SingleLiveEvent
import io.flaterlab.meshexam.androidbase.getLauncher
import io.flaterlab.meshexam.domain.interactor.ProfileInteractor
import io.flaterlab.meshexam.feature.meshroom.dvo.HostResultDvo
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@HiltViewModel
internal class HostResultViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    profileInteractor: ProfileInteractor,
) : BaseViewModel() {

    private val launcher: HostResultLauncher = savedStateHandle.getLauncher()

    val resultInfo = profileInteractor.hostingResultMeta(launcher.hostingId)
        .map { resultModel ->
            HostResultDvo(
                time = SimpleDateFormat("mm:ss", Locale.getDefault())
                    .format(Date(resultModel.durationInMillis)),
                submissionCount = resultModel.submissionCount,
            )
        }
        .catch { e -> e.showLocalizedMessage() }
        .asLiveData(viewModelScope.coroutineContext)

    val commandOpenResults = SingleLiveEvent<String>()
    val commandGoToMain = SingleLiveEvent<Unit>()

    fun onSeeResultsClicked() {
        commandOpenResults.value = launcher.hostingId
    }

    fun onGoToMainClicked() {
        commandGoToMain.call()
    }
}