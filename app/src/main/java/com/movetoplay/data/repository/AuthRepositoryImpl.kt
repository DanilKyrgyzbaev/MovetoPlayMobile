package com.movetoplay.data.repository

import android.util.Log
import com.movetoplay.data.model.AuthBody
import com.movetoplay.data.model.RegBody
import com.movetoplay.domain.repository.AuthRepository
import com.movetoplay.domain.repository.ProfileRepository
import com.movetoplay.domain.utils.RequestStatus
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.http.HttpStatusCode.Companion.BadRequest
import io.ktor.http.HttpStatusCode.Companion.Created

class AuthRepositoryImpl(
    private val profileRepository: ProfileRepository,
    private val client: HttpClient
): AuthRepository {
    override suspend fun signIn(email: String, password: String): RequestStatus {
        val response = client.post("/auth/login"){
            contentType(ContentType.Application.Json)
            setBody(AuthBody(email, password))
        }
        return when(response.status){
            Created -> {
                profileRepository.run {
                    token = response.body<Map<String,String>>()["token"]
                    this.email = email
                    this.password = password
                }
                RequestStatus.Success<Nothing>()
            }
            BadRequest -> RequestStatus.Error<Nothing>(message = response.bodyAsText())
            else -> { RequestStatus.Error<Nothing>() }
        }
    }

    override suspend fun signUp(email: String, password: String, age: Int): RequestStatus {
        val response = client.post("/auth/registration"){
            contentType(ContentType.Application.Json)
            setBody(RegBody(email, password, age))
        }
        Log.e(response.status.toString(),response.bodyAsText())
        return when(response.status){
            Created -> {
                profileRepository.run {
                    token = response.body<Map<String,String>>()["token"]
                    this.email = email
                    this.password = password
                }
                RequestStatus.Success<Nothing>()
            }
            BadRequest -> RequestStatus.Error<Nothing>(message = response.bodyAsText())
            else -> { RequestStatus.Error<Nothing>() }
        }
    }
}