package com.movetoplay.network_api

import com.movetoplay.data.model.AuthorizeProfileBody
import com.movetoplay.data.model.DeviceBody
import com.movetoplay.data.model.ErrorBody
import com.movetoplay.data.model.user_apps.UserAppsBody
import com.movetoplay.domain.model.*
import com.movetoplay.domain.model.user_apps.Limited
import com.movetoplay.domain.model.user_apps.UserApp
import com.movetoplay.model.*
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    // -------------- Auth ----------------------//
    @POST("/auth/login")
    fun postUser(@Body user: User): Call<User>

    @POST("/auth/login")
    fun login(@Body user: User): Call<User>

    @POST("/auth/registration")
    fun postUserRegister(@Body registration: Registration): Call<Registration>

    @PATCH("/accounts/confirm")
    fun confirmAccounts(
        @Header("Authorization") token: String,
        @Body confirm: AccountsConfirm
    ): Call<AccountsConfirm>

    @GET("/auth/rememberPassword")
    fun rememberPassword(
        @Query("email") email: String
    ): Call<RememberPassword>

    @POST("/auth/confirmRememberPasswordCode")
    fun getJwtSessionToken(
        @Body jwtSessionToken: JwtSessionToken
    ): Call<JwtSessionToken>

    @PATCH("/auth/changePasswordByCode")
    fun changePasswordByCode(
        @Header("Authorization") token: String,
        @Body changePasswordByCode: ChangePasswordByCode
    ): Call<ChangePasswordByCode>

    @POST("/auth/authorizeProfile")
    suspend fun authorizeProfile(
        @Header("Authorization") token: String,
        @Body authorize: AuthorizeProfileBody
    ): Response<TokenResponse>

    @POST("/exercises/touch")
    fun sendTouch(
        @Header("Authorization") token: String,
        @Body touch: Touch
    ): Call<Touch>

    // -------------- Profiles ----------------------//

    @POST("/profiles/create")
    suspend fun postChildProfile(
        @Header("Authorization") token: String,
        @Body createProfile: CreateProfile
    ): Response<Child>

    @GET("/profiles/getList")
    suspend fun getChildes(
        @Header("Authorization") token: String
    ): Response<List<Child>>

    // -------------- Account ----------------------//

    @GET("/accounts/getInfo")
    suspend fun geInfo(
        @Header("Authorization") token: String
    ): Response<Parent>

    // -------------- Apps ----------------------//
    @POST("/apps/sync")
    suspend fun postUserApps(
        @Header("Authorization") token: String,
        @Body apps: UserAppsBody
    ): Response<ErrorBody>

    @GET("/apps/getList")
    suspend fun getUserApps(
        @Header("Authorization") token: String,
        @Query("profileId") id: String
    ): Response<List<UserApp>>

    @PATCH("/apps/setLimit")
    suspend fun onLimit(
        @Header("Authorization") token: String,
        @Query("id") id: String,
        @Body limited: Limited
    ): Response<Limited>

    // -------------- Device ----------------------//

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
}
