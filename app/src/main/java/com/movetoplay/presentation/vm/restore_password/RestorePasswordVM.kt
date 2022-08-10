package com.movetoplay.presentation.vm.restore_password

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RestorePasswordVM @Inject constructor(

) : ViewModel() {
    private val _state: MutableState<StateRestorePassword> = mutableStateOf(StateRestorePassword.InputEmail)
    val state: State<StateRestorePassword> get() = _state


    fun onEvent(event: EventRestorePassword){
        when(event){
            is EventRestorePassword.SendEmail -> {
                _state.value = StateRestorePassword.InputCode
            }
            is EventRestorePassword.SendCode -> {
                _state.value = StateRestorePassword.ChangePassword
            }
            is EventRestorePassword.SendNewPassword -> {

            }
        }
    }
}