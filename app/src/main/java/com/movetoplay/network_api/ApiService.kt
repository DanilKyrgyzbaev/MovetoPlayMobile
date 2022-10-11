package com.movetoplay.network_api

import com.movetoplay.model.CreateProfile
import com.movetoplay.model.Registration
import com.movetoplay.model.User
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface ApiService {

    @POST("/auth/registration")
    fun postUser(@Body user: User): Call<User>

    @POST("/auth/registration")
    fun postUserRegister(@Body registration: Registration): Call<Registration>

    @POST("/profiles/create")
    fun postChildProfile(
        @Header("token") token: String,
        @Body createProfile: CreateProfile
    ): Call<CreateProfile>

    ///  @POST("/api/v2/users/update")
    //    fun updateUser(
    //        @Header("token") token: String,
    //        @Body user: User,
    //    ): Call<User>
}