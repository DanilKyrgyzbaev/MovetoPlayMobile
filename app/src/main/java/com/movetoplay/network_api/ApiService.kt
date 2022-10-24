package com.movetoplay.network_api

import com.movetoplay.data.model.AuthorizeProfileBody
import com.movetoplay.data.model.ConfirmBody
import com.movetoplay.data.model.DeviceBody
import com.movetoplay.data.model.ErrorBody
import com.movetoplay.data.model.user_apps.UserAppsBody
import com.movetoplay.domain.model.*
import com.movetoplay.domain.model.user_apps.Limited
import com.movetoplay.domain.model.user_apps.UserApp
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    //-------------- Auth ----------------------//

    @POST("/auth/login")
    suspend fun login(@Body user: User): Response<TokenResponse>

    @POST("/auth/registration")
    suspend fun register(@Body registration: Registration): Response<TokenResponse>

    @POST("/auth/authorizeProfile")
    suspend fun authorizeProfile(
        @Header("Authorization") token: String,
        @Body authorize: AuthorizeProfileBody
    ): Response<TokenResponse>

    @PATCH("/accounts/confirm")
    suspend fun confirmEmail(
        @Header("Authorization") token: String,
        @Body confirmBody: ConfirmBody
    ): Response<Unit>
    //-------------- Profiles ----------------------//

    @POST("/profiles/create")
    suspend fun postChildProfile(
        @Header("Authorization") token: String,
        @Body createProfile: CreateProfile
    ): Response<Child>

    @GET("/profiles/getList")
    suspend fun getChildes(
        @Header("Authorization") token: String,
    ): Response<List<Child>>

    //-------------- Account ----------------------//

    @GET("/accounts/getInfo")
    suspend fun geInfo(
        @Header("Authorization") token: String
    ): Response<Parent>

    //-------------- Apps ----------------------//
    @POST("/apps/sync")
    suspend fun postUserApps(
        @Header("Authorization") token: String,
        @Body apps: UserAppsBody,
    ): Response<ErrorBody>

    @GET("/apps/getList")
    suspend fun getUserApps(
        @Header("Authorization") token: String,
        @Query("profileId") id: String,
    ): Response<List<UserApp>>

    @PATCH("/apps/setLimit")
    suspend fun onLimit(
        @Header("Authorization") token: String,
        @Query("id") id: String,
        @Body limited: Limited,
    ): Response<Limited>

    //-------------- Device ----------------------//

    @POST("/devices/create")
    suspend fun createDevice(
        @Header("Authorization") token: String,
        @Body device: DeviceBody
    ): Response<ChildDevice>

    @GET("/devices/get")
    suspend fun getDevice(
        @Header("Authorization") token: String,
        @Query("id") id: String
    ): Response<ChildDevice>

    @GET("/devices/getByMacAddress")
    suspend fun getDeviceByMac(
        @Header("Authorization") token: String,
        @Query("profileId") profileId: String,
        @Query("macAddress") macAddress: String
    ): Response<ChildDevice>
}