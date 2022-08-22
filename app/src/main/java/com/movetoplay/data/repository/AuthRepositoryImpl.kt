package com.movetoplay.data.repository

import com.movetoplay.data.model.AuthBody
import com.movetoplay.data.model.ErrorBody
import com.movetoplay.data.model.RegBody
import com.movetoplay.domain.repository.AuthRepository
import com.movetoplay.domain.utils.StateProblems
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.http.HttpStatusCode.Companion.BadRequest
import io.ktor.http.HttpStatusCode.Companion.Created
import io.ktor.http.HttpStatusCode.Companion.Unauthorized

class AuthRepositoryImpl(
    private val client: HttpClient
): AuthRepository {
    override suspend fun signIn(email: String, password: String): String {
        val response = client.post("/auth/login"){
            contentType(ContentType.Application.Json)
            setBody(AuthBody(email, password))
        }
        return when(response.status){
            Created ->response.body<Map<String,String>>()["token"]!!
            BadRequest -> throw StateProblems.BadRequest(response.body<ErrorBody>().message)
            Unauthorized -> throw StateProblems.BadRequest(response.body<ErrorBody>().message)
            else -> throw StateProblems.Contingency
        }
    }

    override suspend fun signUp(email: String, password: String, age: Byte): String {
        val response = client.post("/auth/registration"){
            contentType(ContentType.Application.Json)
            setBody(RegBody(email, password, age))
        }
        return when(response.status){
            Created -> response.body<Map<String,String>>()["token"]!!
            BadRequest -> throw StateProblems.BadRequest(response.body<ErrorBody>().message)
            else -> throw StateProblems.Contingency
        }
    }
}