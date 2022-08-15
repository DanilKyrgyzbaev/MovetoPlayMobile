package com.movetoplay.data.repository

import android.util.Log
import com.movetoplay.data.model.AuthBody
import com.movetoplay.data.model.NewProfileChildBody
import com.movetoplay.domain.model.Child
import com.movetoplay.domain.repository.ProfileRepository
import com.movetoplay.domain.repository.ProfilesRepository
import com.movetoplay.domain.utils.RequestStatus
import com.movetoplay.domain.utils.StateProblems
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*

class ProfilesRepositoryImpl(
    private val client: HttpClient,
    private val profileRepository: ProfileRepository
) : ProfilesRepository {
    override suspend fun getListProfileChild(): RequestStatus {
        val response = client.get("/profiles/getList"){
            headers {
                header(HttpHeaders.Authorization, "Bearer " +profileRepository.token)
            }
        }
        return when(response.status){
            HttpStatusCode.OK -> {
                try {
                    return RequestStatus.Success<List<Child>>(data = response.body())
                }catch (e: Exception){
                    RequestStatus.Error<Nothing>()
                }
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
            setBody(child.run { NewProfileChildBody(name,age,gender.name) })
            headers {
                header(HttpHeaders.Authorization,"Bearer " +profileRepository.token)
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