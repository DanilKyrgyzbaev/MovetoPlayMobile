package com.movetoplay.data.repository

import com.movetoplay.data.mapper.toApiError
import com.movetoplay.data.model.*
import com.movetoplay.domain.model.Registration
import com.movetoplay.domain.model.TokenResponse
import com.movetoplay.domain.model.User
import com.movetoplay.domain.repository.AuthRepository
import com.movetoplay.domain.utils.ResultStatus
import com.movetoplay.network_api.ApiService
import com.movetoplay.pref.Pref
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val api: ApiService
) : AuthRepository {

    override suspend fun signIn(email: String, password: String): ResultStatus<TokenResponse> {
        return try {
            val response = api.login(
                User(email, password)
            )
            if (response.isSuccessful) ResultStatus.Success(response.body())
            else ResultStatus.Error(response.errorBody().toApiError<ErrorBody>().message)

        } catch (e: Throwable) {
            ResultStatus.Error(e.localizedMessage)
        }
    }

    override suspend fun signUp(
        email: String,
        password: String,
        age: Int
    ): ResultStatus<TokenResponse> {
        return try {
            val response = api.register(
                Registration(email, password, age)
            )

            if (response.isSuccessful) ResultStatus.Success(response.body())
            else ResultStatus.Error(response.errorBody().toApiError<ErrorBody>().message)

        } catch (e: Throwable) {
            ResultStatus.Error(e.localizedMessage)
        }
    }

    override suspend fun authorizeProfile(
        childId: String,
        deviceId: String
    ): ResultStatus<TokenResponse> {
        return try {
            val response = api.authorizeProfile(
                "Bearer ${Pref.userAccessToken}",
                AuthorizeProfileBody(deviceId, childId)
            )

            if (response.isSuccessful) ResultStatus.Success(response.body())
            else ResultStatus.Error(response.errorBody().toApiError<ErrorBody>().message)

        } catch (e: Throwable) {
            ResultStatus.Error(e.localizedMessage)
        }
    }

    override suspend fun confirm(code: Int): ResultStatus<Boolean> {
        return try {
            val response = api.confirmEmail(
                "Bearer ${Pref.userAccessToken}",
                ConfirmBody(code)
            )

            if (response.isSuccessful) ResultStatus.Success(true)
            else ResultStatus.Error(response.errorBody().toApiError<ErrorBody>().message)

        } catch (e: Throwable) {
            ResultStatus.Error(e.localizedMessage)
        }
    }

    override suspend fun loginViaGoogle(token: String): ResultStatus<TokenResponse> {
        return try {
            val response = api.loginViaGoogle(
                LoginViaGoogleBody(token)
            )

            if (response.isSuccessful) ResultStatus.Success(response.body())
            else ResultStatus.Error(response.errorBody().toApiError<ErrorBody>().message)

        } catch (e: Throwable) {
            ResultStatus.Error(e.localizedMessage)
        }
    }

    override suspend fun registerViaGoogle(token: String): ResultStatus<TokenResponse> {
        return try {
            val response = api.registerViaGoogle(
                LoginViaGoogleBody(token)
            )

            if (response.isSuccessful) ResultStatus.Success(response.body())
            else ResultStatus.Error(response.errorBody().toApiError<ErrorBody>().message)

        } catch (e: Throwable) {
            ResultStatus.Error(e.localizedMessage)
        }
    }
}