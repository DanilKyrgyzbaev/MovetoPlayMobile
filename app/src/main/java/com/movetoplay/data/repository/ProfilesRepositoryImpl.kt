package com.movetoplay.data.repository

import com.movetoplay.data.model.AuthBody
import com.movetoplay.domain.model.Child
import com.movetoplay.domain.repository.ProfileRepository
import com.movetoplay.domain.repository.ProfilesRepository
import com.movetoplay.domain.utils.RequestStatus
import com.movetoplay.domain.utils.StateProblems
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*

class ProfilesRepositoryImpl(
    private val client: HttpClient,
    private val profileRepository: ProfileRepository
) : ProfilesRepository {
    override suspend fun getListProfileChild(): RequestStatus {
        val response = client.get("/profiles/create"){
            headers {
                header(HttpHeaders.Authorization,profileRepository.token)
            }
        }
        return when(response.status){
            HttpStatusCode.Created -> {
                RequestStatus.Success<List<Child>>(data = response.body())
            }
            HttpStatusCode.Unauthorized -> {
                RequestStatus.Error(data = StateProblems.NeedRestoreSession)
            }
            else -> { RequestStatus.Error<Nothing>() }
        }
    }

    override suspend fun createProfileChild(child: Child): RequestStatus {
        val response = client.post("/profiles/create"){
            contentType(ContentType.Application.Json)
            setBody(child)
            headers {
                header(HttpHeaders.Authorization,profileRepository.token)
            }
        }
        return when(response.status){
            HttpStatusCode.Created -> {
                RequestStatus.Success<Child>(data = response.body())
            }
            HttpStatusCode.Unauthorized -> {
                RequestStatus.Error(data = StateProblems.NeedRestoreSession)
            }
            else -> { RequestStatus.Error<Nothing>() }
        }
    }

}