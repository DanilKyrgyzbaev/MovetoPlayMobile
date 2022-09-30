package com.movetoplay.presentation.vm.app_vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.movetoplay.domain.manager.StateUserApp
import com.movetoplay.domain.manager.StateUserAppManager
import com.movetoplay.domain.use_case.session.RestoreSessionUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class AppVM @Inject constructor(
    private val stateUserAppManager: StateUserAppManager,
    private val restoreSessionUseCase: RestoreSessionUseCase
) : ViewModel() {
    val stateUserApp: Flow<StateUserApp> get() = stateUserAppManager.stateUserApp
    init {
        viewModelScope.async(Dispatchers.IO) {
            restoreSessionUseCase(initializingState = true)
        }.start()
    }
}