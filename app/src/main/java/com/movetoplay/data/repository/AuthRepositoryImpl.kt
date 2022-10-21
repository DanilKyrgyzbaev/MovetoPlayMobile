package com.movetoplay.data.repository

import com.movetoplay.data.model.AuthBody
import com.movetoplay.data.model.AuthorizeProfileBody
import com.movetoplay.data.model.ErrorBody
import com.movetoplay.data.model.RegBody
import com.movetoplay.domain.model.TokenResponse
import com.movetoplay.domain.repository.AuthRepository
import com.movetoplay.domain.utils.ResultStatus
import com.movetoplay.domain.utils.StateProblems
import com.movetoplay.network_api.ApiService
import com.movetoplay.pref.Pref
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.http.HttpStatusCode.Companion.BadRequest
import io.ktor.http.HttpStatusCode.Companion.Created
import io.ktor.http.HttpStatusCode.Companion.Unauthorized
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val client: HttpClient,
    private val api: ApiService
) : AuthRepository {
    override suspend fun signIn(email: String, password: String): String {
        val response = client.post("/auth/login") {
            contentType(ContentType.Application.Json)
            setBody(AuthBody(email, password))
        }
        return when (response.status) {
            Created -> response.body<Map<String, String>>()["token"]!!
            BadRequest -> throw StateProblems.BadRequest(response.body<ErrorBody>().message)
            Unauthorized -> throw StateProblems.BadRequest(response.body<ErrorBody>().message)
            else -> throw StateProblems.Contingency
        }
    }

    override suspend fun signUp(email: String, password: String, age: Byte): String {
        val response = client.post("/auth/registration") {
            contentType(ContentType.Application.Json)
            setBody(RegBody(email, password, age))
        }
        return when (response.status) {
            Created -> response.body<Map<String, String>>()["token"]!!
            BadRequest -> throw StateProblems.BadRequest(response.body<ErrorBody>().message)
            else -> throw StateProblems.Contingency
        }
    }

    override suspend fun authorizeProfile(
        childId: String,
        deviceId: String
    ): ResultStatus<TokenResponse> {
        return try {
            val response = api.authorizeProfile(
                "Bearer ${Pref.userToken}",
                AuthorizeProfileBody(deviceId, childId)
            )

            if (response.isSuccessful) ResultStatus.Success(response.body())
            else ResultStatus.Error(response.errorBody().toString())

        } catch (e: Throwable) {
            ResultStatus.Error(e.localizedMessage)
        }
    }
}