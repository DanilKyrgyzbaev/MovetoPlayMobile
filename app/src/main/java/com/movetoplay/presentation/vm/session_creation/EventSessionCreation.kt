package com.movetoplay.presentation.vm.session_creation

sealed class EventSessionCreation {
    object SignInViaGoogle: EventSessionCreation()
    object SignIn : EventSessionCreation()
    object SignUp : EventSessionCreation()
}