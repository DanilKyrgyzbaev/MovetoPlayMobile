package com.movetoplay.presentation.vm.session_creation

sealed class StateSessionCreation {
    object InputData : StateSessionCreation()
    object Success : StateSessionCreation()
}