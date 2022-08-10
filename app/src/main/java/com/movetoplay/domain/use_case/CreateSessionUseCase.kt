package com.movetoplay.domain.use_case

import com.movetoplay.domain.repository.AuthRepository
import com.movetoplay.domain.utils.RequestStatus
import com.movetoplay.domain.utils.StateSessionCreation
import com.movetoplay.domain.utils.TypesRegularExpressions

class CreateSessionUseCase(
    private val authRepository: AuthRepository,
    private val regularExpressionsUseCase: RegularExpressionsUseCase
) {
    suspend operator fun invoke(
        email: String,
        password: String,
        repeatPassword: String?
    ) : RequestStatus  {
        val isErrorEmail = !regularExpressionsUseCase(email,TypesRegularExpressions.Email)
        val isErrorPassword = !regularExpressionsUseCase(password,TypesRegularExpressions.Password)
        val isErrorRepeatPassword = repeatPassword?.let {
            !regularExpressionsUseCase(it,TypesRegularExpressions.RepeatPassword(password))
        }
        if(isErrorEmail || isErrorPassword || isErrorRepeatPassword == true){
            return RequestStatus.Error(
                StateSessionCreation(
                    isErrorEmail = isErrorEmail,
                    isErrorPassword = isErrorPassword,
                    isErrorRepeatPassword = isErrorRepeatPassword)
            )
        }
        return if (repeatPassword != null){
            authRepository.signUp(email, password)
        }else{
            authRepository.signIn(email, password)
        }
    }
}