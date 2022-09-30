package com.movetoplay.presentation.vm.restore_password

import com.movetoplay.domain.utils.TypesRegularExpressions

sealed class EventRestorePassword {
    class SendEmail(val email: String) : EventRestorePassword()
    class SendCode(val code: String) : EventRestorePassword()
    class SendNewPassword(val newPassword: String, val repeatPassword: String) : EventRestorePassword()
}