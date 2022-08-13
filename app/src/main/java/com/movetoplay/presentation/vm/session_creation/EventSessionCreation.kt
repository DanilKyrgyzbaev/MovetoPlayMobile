package com.movetoplay.presentation.vm.session_creation

import com.movetoplay.domain.model.Role

sealed class EventSessionCreation {
    object StopSignInViaGoogle: EventSessionCreation()
    class SignIn(val email : String, val password : String, val role: Role) : EventSessionCreation()
    class SignUp(val email : String, val password : String, val age: Int) : EventSessionCreation()
}