package com.movetoplay.domain.utils

data class StateSessionCreation(
    val isErrorEmail: Boolean = false,
    val isErrorPassword: Boolean = false,
    val isErrorRepeatPassword: Boolean? = null,
)