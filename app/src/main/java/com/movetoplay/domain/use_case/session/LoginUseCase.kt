package com.movetoplay.domain.use_case.session

import com.movetoplay.domain.manager.StateUserAppManager
import com.movetoplay.domain.model.Role
import com.movetoplay.domain.repository.ProfileRepository

class LoginUseCase(
    private val profileRepository: ProfileRepository,
    private val stateUserAppManager: StateUserAppManager
) {
    operator fun invoke(
        token: String,
        role: Role,
        email: String,
        password: String
    ){
        profileRepository.token = token
        profileRepository.role = role
        profileRepository.email = email
        profileRepository.password = password
        stateUserAppManager.initializingState()
    }
}