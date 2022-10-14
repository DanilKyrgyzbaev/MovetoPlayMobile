package com.movetoplay.network_api

import com.movetoplay.data.model.ErrorBody
import com.movetoplay.data.model.user_apps.UserAppsBody
import com.movetoplay.domain.model.user_apps.Limited
import com.movetoplay.domain.model.user_apps.UserApps
import com.movetoplay.model.CreateProfile
import com.movetoplay.model.Registration
import com.movetoplay.model.ResponseSucces
import com.movetoplay.model.User
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    @POST("/auth/login")
    fun postUser(@Body user: User): Call<User>

    @POST("/auth/login")
    fun login(@Body user: User): Call<User>

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

    @POST("/apps/sync")
    suspend fun postUserApps(
        @Header("token") token: String,
        @Body apps: UserAppsBody,
    ): Response<ErrorBody>

    @GET("/apps/sync")
    suspend fun getUserApps(
        @Header("token") token: String,
    ): Response<UserApps>

    @PATCH("/apps/setLimit?id={id}")
    suspend fun onLimit(
        @Path("id") id: String,
        @Body limited: Limited,
    ): Response<Limited>
}