package com.movetoplay.domain.manager

import com.movetoplay.domain.model.Role
import com.movetoplay.domain.repository.ProfileRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class StateUserAppManager(
    private val profileRepository: ProfileRepository
){
    private val flow by lazy {
        MutableStateFlow<StateUserApp>(StateUserApp.Definition)
    }
    val stateUserApp : Flow<StateUserApp> get() = flow

    fun initializingState(){
        when(profileRepository.role){
            Role.Parent -> flow.value = StateUserApp.Parent
            Role.Children ->{
                flow.value = StateUserApp.Children(
                    selectedProfileChild = profileRepository.child != null
                )
            }
            null -> flow.value = StateUserApp.NotAuthorized
        }
    }
    fun disconnect(){
        profileRepository.clear()
        flow.value = StateUserApp.NotAuthorized
    }

}