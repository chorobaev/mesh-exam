package io.flaterlab.meshexam.presentation.discover

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.flaterlab.meshexam.androidbase.BaseViewModel
import io.flaterlab.meshexam.domain.usecase.DiscoverExamsUseCase
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class DiscoverViewModel @Inject constructor(
    private val discoverExamsUseCase: DiscoverExamsUseCase,
) : BaseViewModel() {

    init {
        discoverExamsUseCase()
            .onEach {
                Timber.d(it.toString())
            }
            .launchIn(viewModelScope)

    }

    fun onRefreshClicked() {

    }
}