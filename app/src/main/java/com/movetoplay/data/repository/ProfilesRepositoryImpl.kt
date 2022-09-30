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
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class ProfilesRepositoryImpl(
    private val client: HttpClient,
    private val profileRepository: ProfileRepository
) : ProfilesRepository {
    override suspend fun getListProfileChild(): List<Child> {
        val response = client.get("/profiles/getList"){
            headers {
                header(HttpHeaders.Authorization, "Bearer " +profileRepository.token)
            }
        }
        return when(response.status){
            HttpStatusCode.OK -> response.body()
            HttpStatusCode.Unauthorized -> throw StateProblems.NeedRestoreSession
            else -> throw StateProblems.Contingency
        }
    }

    override suspend fun createProfileChild(child: Child): Child {
        val response = client.post("/profiles/create"){
            contentType(ContentType.Application.Json)
            setBody(child.run { NewProfileChildBody(name,age,gender.name) })
            headers {
                header(HttpHeaders.Authorization,"Bearer " +profileRepository.token)
            }
        }
        return when(response.status){
            HttpStatusCode.OK -> response.body()
            HttpStatusCode.Unauthorized -> throw StateProblems.NeedRestoreSession
            else -> throw StateProblems.Contingency
        }
    }

}