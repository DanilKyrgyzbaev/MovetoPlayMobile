package com.movetoplay.data.repository

import com.movetoplay.data.model.ErrorBody
import com.movetoplay.data.model.user_apps.UserAppsBody
import com.movetoplay.domain.model.user_apps.Limited
import com.movetoplay.domain.model.user_apps.UserApp
import com.movetoplay.domain.repository.UserAppsRepository
import com.movetoplay.domain.utils.ResultStatus
import com.movetoplay.network_api.ApiService
import com.movetoplay.pref.Pref
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withTimeout
import javax.inject.Inject

const val NETWORK_TIMEOUT = 6000L

class UserAppsRepositoryImpl @Inject constructor(private val client: ApiService) :
    UserAppsRepository {

    override suspend fun postLimitedApps(
        token: String,
        apps: UserAppsBody
    ): Flow<ResultStatus<ErrorBody>> =
        flow {
            try {
                emit(ResultStatus.Loading())
                withTimeout(NETWORK_TIMEOUT) {
                    val response = client.postUserApps("Bearer $token", apps)
                    if (response.isSuccessful) emit(ResultStatus.Success(response.body()))
                    else emit(ResultStatus.Error(response.message()))
                }
            } catch (throwable: Throwable) {
                emit(ResultStatus.Error(throwable.message))
            }
        }.flowOn(Dispatchers.IO)


    override suspend fun getLimitedApps(
        token: String,
        id: String
    ): Flow<ResultStatus<List<UserApp>>> =
        flow {
            try {
                emit(ResultStatus.Loading())

                withTimeout(NETWORK_TIMEOUT) {
                    val response = client.getUserApps("Bearer ${Pref.userToken}", id)
                    if (response.isSuccessful) emit(ResultStatus.Success(data = response.body()))
                    else emit(ResultStatus.Error(response.message()))
                }
            } catch (throwable: Throwable) {
                emit(ResultStatus.Error(throwable.message))
            }
        }.flowOn(Dispatchers.IO)


    override suspend fun setLimitedApp(id: String, limited: Limited): Flow<ResultStatus<Boolean>> =
        flow {
            try {
                emit(ResultStatus.Loading())
                withTimeout(NETWORK_TIMEOUT) {
                    val response = client.onLimit("Bearer ${Pref.childToken}", id, limited)
                    if (response.isSuccessful) emit(ResultStatus.Success(true))
                    else emit(ResultStatus.Error(response.errorBody().toString()))
                }
            } catch (throwable: Throwable) {
                emit(ResultStatus.Error(throwable.message))
            }
        }.flowOn(Dispatchers.IO)

}