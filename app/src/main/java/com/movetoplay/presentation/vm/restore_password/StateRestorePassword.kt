package com.movetoplay.presentation.vm.restore_password

sealed class StateRestorePassword{
    object InputEmail : StateRestorePassword()
    object InputCode : StateRestorePassword()
    object ChangePassword : StateRestorePassword()
}
