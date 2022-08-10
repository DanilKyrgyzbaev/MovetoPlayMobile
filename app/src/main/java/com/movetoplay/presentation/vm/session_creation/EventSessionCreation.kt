package com.movetoplay.presentation.vm.session_creation

sealed class EventSessionCreation {
    object SignInViaGoogle : EventSessionCreation()
    object StopSignInViaGoogle: EventSessionCreation()
    class SignIn(val email : String, val password : String) : EventSessionCreation()
    class SignUp(val email : String, val password : String, val repeatPassword: String) : EventSessionCreation()
}