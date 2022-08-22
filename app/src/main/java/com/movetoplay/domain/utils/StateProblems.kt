package com.movetoplay.domain.utils


sealed class StateProblems : Throwable(){
    object NeedRestoreSession: StateProblems()
    class BadRequest(override val message: String?) : StateProblems()
    object Contingency : StateProblems()
}
