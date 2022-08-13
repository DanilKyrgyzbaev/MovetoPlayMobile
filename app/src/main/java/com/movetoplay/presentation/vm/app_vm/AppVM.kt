package com.movetoplay.presentation.vm.app_vm

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.movetoplay.domain.model.Role
import com.movetoplay.domain.repository.ProfileRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AppVM @Inject constructor(
    private val profileRepository: ProfileRepository
) : ViewModel() {
    private val _stateUserApp = mutableStateOf<StateUserApp>(StateUserApp.Definition)
    val stateUserApp: State<StateUserApp> get() = _stateUserApp
    init {
        initializingState()
    }
    private fun initializingState(){
        when(profileRepository.role){
            Role.Parent -> _stateUserApp.value = StateUserApp.Parent
            Role.Children -> _stateUserApp.value = StateUserApp.Children
            null -> _stateUserApp.value = StateUserApp.NotAuthorized
        }
    }
    fun onEvent(event: EventApp){
        when(event){
            EventApp.UserAuth ->{
                initializingState()
            }
        }
    }
}