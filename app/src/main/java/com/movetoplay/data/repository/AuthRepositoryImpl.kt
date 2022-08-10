package com.movetoplay.data.repository

import com.movetoplay.data.model.AuthBody
import com.movetoplay.domain.repository.AuthRepository
import com.movetoplay.domain.utils.RequestStatus
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.http.HttpStatusCode.Companion.Created

class AuthRepositoryImpl(
    private val tokenRepositoryImpl: TokenRepositoryImpl,
    private val client: HttpClient
): AuthRepository {
    override suspend fun signIn(email: String, password: String): RequestStatus {
//        val response = client.post(""){
//            contentType(ContentType.Application.Json)
//            setBody(AuthBody(email, password))
//        }
//        return when(response.status){
//            Created -> RequestStatus.Success<Nothing>()
//            else -> { RequestStatus.Error<Nothing>() }
//        }
        return RequestStatus.Success<Nothing>()
    }

    override suspend fun signUp(email: String, password: String): RequestStatus {
        TODO("Not yet implemented")
    }
}