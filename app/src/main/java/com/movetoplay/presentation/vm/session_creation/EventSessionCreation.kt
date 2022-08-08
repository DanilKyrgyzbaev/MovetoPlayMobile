package com.movetoplay.presentation.vm.session_creation

sealed class EventSessionCreation {
    object SignUpReset: EventSessionCreation()
    object SignInReset: EventSessionCreation()
    object SignInGoogle :  EventSessionCreation()
    object SignInGoogleReset :  EventSessionCreation()
    class  SignUp(val email: String, val  password: String, val repeatPassword: String) : EventSessionCreation()
    class SignIn(val email: String, val  password: String) : EventSessionCreation()
}