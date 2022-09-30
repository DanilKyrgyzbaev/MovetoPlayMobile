package com.movetoplay.domain.use_case.session

import com.movetoplay.domain.manager.StateUserAppManager
import com.movetoplay.domain.repository.AuthRepository
import com.movetoplay.domain.repository.ProfileRepository
import com.movetoplay.domain.use_case.HasConnectionUseCase
import com.movetoplay.domain.utils.StateProblems

class RestoreSessionUseCase(
    private val profileRepository: ProfileRepository,
    private val stateUserAppManager: StateUserAppManager,
    private val authRepository: AuthRepository,
) {
    suspend operator fun invoke(initializingState: Boolean = false): Boolean{
        val email = profileRepository.email
        val password = profileRepository.password
        return try {
            if (email != null && password != null)
                profileRepository.token = authRepository.signIn(email,password)
            if (initializingState)
                stateUserAppManager.initializingState()
            true
        }catch (e: StateProblems.BadRequest){
            stateUserAppManager.disconnect()
            false
        }catch (e: StateProblems.Contingency){
            false
        }
    }
}