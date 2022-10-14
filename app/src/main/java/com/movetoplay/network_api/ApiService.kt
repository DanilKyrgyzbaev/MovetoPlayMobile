package com.movetoplay.network_api

import com.movetoplay.model.CreateProfile
import com.movetoplay.model.Registration
import com.movetoplay.model.ResponseSucces
import com.movetoplay.model.User
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface ApiService {
    @POST("/auth/login")
    fun login(@Body user: User): Call<User>

    @POST("/auth/registration")
    fun postUserRegister(@Body registration: Registration): Call<Registration>

    @POST("/profiles/create")
    fun postChildProfile(
        @Header("Authorization") token: String,
        @Body createProfile: CreateProfile
    ): Call<CreateProfile>
}