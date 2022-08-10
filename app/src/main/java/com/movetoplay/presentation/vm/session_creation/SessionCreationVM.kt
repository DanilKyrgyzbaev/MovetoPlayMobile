package com.movetoplay.presentation.vm.session_creation

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.movetoplay.domain.use_case.CreateSessionUseCase
import com.movetoplay.domain.utils.RequestStatus
import com.movetoplay.domain.utils.StateSessionCreation
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SessionCreationVM @Inject constructor(
    private val createSessionUseCase : CreateSessionUseCase
) : ViewModel() {
    private var createSessionJob: Job?=null
    private val _state : MutableState<StateSessionCreation> = mutableStateOf(StateSessionCreation())
    val state : State<StateSessionCreation> get() = _state
    private val _status : MutableState<RequestStatus> = mutableStateOf(RequestStatus.Null)
    val status : State<RequestStatus> get()= _status
    fun onEvent(event: EventSessionCreation){
        when(event){
            is EventSessionCreation.SignIn ->{
                _status.value = RequestStatus.Loading<Any>()
                createSession(event.email,event.password, null)
            }
            is EventSessionCreation.SignUp ->{
                _status.value = RequestStatus.Loading<Any>()
                createSession(event.email,event.password, event.repeatPassword)
            }
            EventSessionCreation.SignInViaGoogle ->{
                _status.value = RequestStatus.Loading<Any>()
            }
            EventSessionCreation.StopSignInViaGoogle -> {
                _status.value = RequestStatus.Null
            }
        }
    }
    private fun createSession(email: String,
                              password: String,
                              repeatPassword: String?){
        createSessionJob?.cancel()
        createSessionJob  = viewModelScope.async(Dispatchers.IO){
            val answer = createSessionUseCase(email, password, repeatPassword)
            withContext(Dispatchers.Main){
                _status.value = answer
                when(answer){
                    is RequestStatus.Error<*> ->{
                        if(answer.data is StateSessionCreation){
                            _state.value = answer.data
                        }
                    }
                    else -> {}
                }
            }
        }
    }
}