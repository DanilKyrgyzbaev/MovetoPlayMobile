package com.movetoplay.domain.use_case.session

import android.content.Context
import com.movetoplay.R
import com.movetoplay.domain.model.TokenResponse
import com.movetoplay.domain.repository.AuthRepository
import com.movetoplay.domain.use_case.RegularExpressionsUseCase
import com.movetoplay.domain.utils.RequestStatus
import com.movetoplay.domain.utils.ResultStatus
import com.movetoplay.domain.utils.StateProblems
import com.movetoplay.domain.utils.TypesRegularExpressions

class CreateSessionUseCase(
    private val appContext: Context,
    private val authRepository: AuthRepository,
    private val regularExpressionsUseCase: RegularExpressionsUseCase
) {
    suspend operator fun invoke(
        email: String,
        password: String,
        age: Int?,
        sessionCreated: (TokenResponse) -> Unit,
        errorMessage: (message: String?) -> Unit
    ) {
        val isErrorEmail = !regularExpressionsUseCase(email, TypesRegularExpressions.Email)
        val isErrorPassword = !regularExpressionsUseCase(password, TypesRegularExpressions.Password)
        if (isErrorEmail || isErrorPassword) {
            val message = if (isErrorEmail) appContext.getString(R.string.wrong_email)
            else appContext.getString(R.string.password_must_be_greater_than_five_chars)
            errorMessage(message)
        } else {
            try {
                val token =
                    age?.let { authRepository.signUp(email, password, it) }
                 ?: authRepository.signIn(email, password)
                if (token is ResultStatus.Success && token.data != null)
                    sessionCreated(token.data)
            } catch (e: StateProblems.BadRequest) {
                errorMessage(e.message)
            } catch (e: StateProblems.Contingency) {
                errorMessage(appContext.getString(R.string.connection_error))
            }
        }
    }
}