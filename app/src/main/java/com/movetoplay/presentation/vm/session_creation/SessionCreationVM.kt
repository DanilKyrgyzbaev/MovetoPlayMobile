package com.movetoplay.presentation.vm.session_creation

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.movetoplay.domain.model.Role
import com.movetoplay.domain.repository.ProfileRepository
import com.movetoplay.domain.use_case.CreateSessionUseCase
import com.movetoplay.domain.utils.RequestStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SessionCreationVM @Inject constructor(
    private val profileRepository: ProfileRepository,
    private val createSessionUseCase : CreateSessionUseCase
) : ViewModel() {
    private var createSessionJob: Job?=null
    private val _state : MutableState<StateSessionCreation> = mutableStateOf(StateSessionCreation.InputData)
    val state : State<StateSessionCreation> get() = _state
    private val _message: MutableState<String?> = mutableStateOf(null)
    val message: State<String?> get() = _message

    fun onEvent(event: EventSessionCreation){
        createSessionJob?.cancel()
        when(event){
            is EventSessionCreation.SignIn ->{
                createSessionJob  = viewModelScope.async(Dispatchers.IO){
                    val answer = event.run { createSessionUseCase(email,password, null) }
                    withContext(Dispatchers.Main){
                        when(answer){
                            is RequestStatus.Error<*> -> {
                                _message.value = answer.message
                            }
                            is RequestStatus.Success<*> -> {
                                profileRepository.role = event.role
                                _state.value = StateSessionCreation.Success
                            }
                            else -> {}
                        }
                    }
                }
            }
            is EventSessionCreation.SignUp ->{
                createSessionJob  = viewModelScope.async(Dispatchers.IO){
                    val answer = event.run { createSessionUseCase(email,password, age) }
                    withContext(Dispatchers.Main){
                        when(answer){
                            is RequestStatus.Error<*> -> {
                                _message.value = answer.message
                            }
                            is RequestStatus.Success<*> -> {
                                profileRepository.role = Role.Parent
                                _state.value = StateSessionCreation.Success
                            }
                            else -> {}
                        }
                    }
                }
            }
            EventSessionCreation.StopSignInViaGoogle -> {
                _state.value = StateSessionCreation.InputData
            }
        }
    }
}