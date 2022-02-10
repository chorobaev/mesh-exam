package io.flaterlab.meshexam.presentation.discover.ui.info

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.flaterlab.meshexam.androidbase.BaseViewModel
import io.flaterlab.meshexam.androidbase.SingleLiveEvent
import io.flaterlab.meshexam.androidbase.getLauncher
import io.flaterlab.meshexam.androidbase.text.Text
import io.flaterlab.meshexam.domain.interactor.ProfileInteractor
import io.flaterlab.meshexam.presentation.discover.R
import io.flaterlab.meshexam.presentation.discover.dvo.ExamInfoItemDvo
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class ExamInfoViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val profileInteractor: ProfileInteractor
) : BaseViewModel() {

    private val launcher: ExamInfoLauncher = savedStateHandle.getLauncher()

    val examName = MutableLiveData(launcher.examName)
    val examInfoItemList = profileInteractor.userProfile()
        .map { profile ->
            provideInfoList().apply {
                add(
                    ExamInfoItemDvo(
                        title = Text.from(R.string.discover_examInfo_clientTitle),
                        value = Text.from(profile.fullName.ifBlank { "-" }),
                        isEditable = true,
                        onEdit = {
                            commandEditClient.value =
                                Text.from(R.string.profile_edit_editProfileTitle)
                        }
                    )
                )
            }
        }
        .catch { it.showLocalizedMessage() }
        .asLiveData()

    val commandEditClient = SingleLiveEvent<Text>()
    val commandJoinExam = SingleLiveEvent<String>()

    private fun provideInfoList() = mutableListOf(
        ExamInfoItemDvo(
            title = Text.from(R.string.discover_examInfo_hostTitle),
            value = Text.from(launcher.examHostName)
        ),
        ExamInfoItemDvo(
            title = Text.from(R.string.discover_examInfo_durationTitle),
            value = Text.from(R.string.common_string_min, launcher.examDurationInMin.toString())
        )
    )

    fun onJoinClicked() {
        viewModelScope.launch {
            if (profileInteractor.isProfileSetUp()) {
                commandJoinExam.value = launcher.examId
            } else {
                commandEditClient.value = Text.from(R.string.exam_join_createProfile)
            }
        }
    }
}