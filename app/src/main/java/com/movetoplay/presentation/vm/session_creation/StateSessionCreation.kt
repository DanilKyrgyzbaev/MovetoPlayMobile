package com.movetoplay.presentation.vm.session_creation

data class StateSessionCreation(
    val isRegistration : Boolean = false,
    val isErrorEmail: Boolean = false,
    val isErrorPassword: Boolean = false,
    val isErrorRepeatPassword: Boolean? = null,
)