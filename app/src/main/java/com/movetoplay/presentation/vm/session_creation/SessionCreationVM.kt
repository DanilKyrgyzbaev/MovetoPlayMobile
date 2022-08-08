package com.movetoplay.presentation.vm.session_creation

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.movetoplay.domain.model.RequestStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SessionCreationVM @Inject constructor(

) : ViewModel() {
    private val _state : MutableState<StateSessionCreation> = mutableStateOf(StateSessionCreation())
    val state : State<StateSessionCreation> get() = _state
    private val _status : MutableState<RequestStatus> = mutableStateOf(RequestStatus.Null)
    val status : State<RequestStatus> get()= _status
    fun onEvent(event: EventSessionCreation){
        when(event){
            is EventSessionCreation.SignInReset -> {
                _state.value = StateSessionCreation(isRegistration = false)
                _status.value = RequestStatus.Null
            }
            is EventSessionCreation.SignUpReset ->{
                _state.value = StateSessionCreation(isRegistration = true, isErrorRepeatPassword = false)
                _status.value = RequestStatus.Null
            }
            is EventSessionCreation.SignIn ->{
                _status.value = RequestStatus.Loading()
            }
            is EventSessionCreation.SignUp ->{
                _status.value = RequestStatus.Loading()
            }
            EventSessionCreation.SignInGoogle ->{
                _status.value = RequestStatus.Loading()
            }
            EventSessionCreation.SignInGoogleReset ->{
                _status.value = RequestStatus.Null
            }
        }
    }
}