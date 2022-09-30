package com.movetoplay.presentation.vm.session_creation

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.movetoplay.domain.repository.ProfileRepository
import com.movetoplay.domain.use_case.session.CreateSessionUseCase
import com.movetoplay.domain.use_case.session.LoginUseCase
import com.movetoplay.domain.utils.RequestStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import javax.inject.Inject

@HiltViewModel
class SessionCreationVM @Inject constructor(
    private val createSessionUseCase : CreateSessionUseCase,
    private val loginUseCase: LoginUseCase
) : ViewModel() {
    private var createSessionJob: Job?=null
    var message: MutableState<String?> = mutableStateOf(null)
    val form = Form()

    fun onEvent(event: EventSessionCreation){
        when(event){
            EventSessionCreation.SignIn ->{
                auth()
            }
            EventSessionCreation.SignUp ->{
               auth()
            }
            EventSessionCreation.SignInViaGoogle -> {

            }
        }
    }
    private fun auth(){
        createSessionJob?.cancel()
        createSessionJob  = viewModelScope.async(Dispatchers.IO){
            form.run {
                createSessionUseCase(
                    email,password, age,
                    sessionCreated = { token-> loginUseCase(token, role, email, password)},
                    errorMessage = message.component2()
                )
            }
        }
    }
}