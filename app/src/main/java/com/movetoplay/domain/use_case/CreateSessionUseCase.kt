package com.movetoplay.domain.use_case

import android.content.Context
import com.movetoplay.R
import com.movetoplay.domain.repository.AuthRepository
import com.movetoplay.domain.utils.RequestStatus
import com.movetoplay.domain.utils.TypesRegularExpressions

class CreateSessionUseCase(
    private val appContext: Context,
    private val authRepository: AuthRepository,
    private val regularExpressionsUseCase: RegularExpressionsUseCase
) {
    suspend operator fun invoke(
        email: String,
        password: String,
        age: Int?
    ) : RequestStatus  {
        val isErrorEmail = !regularExpressionsUseCase(email,TypesRegularExpressions.Email)
        val isErrorPassword = !regularExpressionsUseCase(password,TypesRegularExpressions.Password)
        if(isErrorEmail || isErrorPassword){
            return RequestStatus.Error<Nothing>(
                message = if(isErrorEmail) appContext.getString(R.string.wrong_email)
                else appContext.getString(R.string.password_must_be_greater_than_five_chars)
            )
        }
        return age?.let {
            authRepository.signUp(email, password, it)
        } ?: authRepository.signIn(email, password)
    }
}