package com.movetoplay.data.repository

import com.movetoplay.data.model.user_apps.UserAppsBody
import com.movetoplay.domain.model.user_apps.Limited
import com.movetoplay.domain.repository.UserAppsRepository
import com.movetoplay.domain.utils.RequestStatus
import com.movetoplay.network_api.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withTimeout
import javax.inject.Inject

const val NETWORK_TIMEOUT = 6000L

class UserAppsRepositoryImpl @Inject constructor(val client: ApiService) :
    UserAppsRepository {

    override suspend fun postLimitedApps(token: String, apps: UserAppsBody): Flow<RequestStatus> =
        flow {
            try {
                emit(RequestStatus.Loading(data = null))
                withTimeout(NETWORK_TIMEOUT) {
                    val response = client.postUserApps(token,apps)
                    if (response.isSuccessful) emit(RequestStatus.Success<String>())
                    else emit(RequestStatus.Error(null, response.message()))
                }
            } catch (throwable: Throwable) {
                emit(RequestStatus.Error(null, throwable.message))
            }
        }.flowOn(Dispatchers.IO)


    override suspend fun getLimitedApps(token: String): Flow<RequestStatus> =
        flow {
            try {
                emit(RequestStatus.Loading<Boolean>())

                withTimeout(NETWORK_TIMEOUT) {
                    val response = client.getUserApps(token)
                    if (response.isSuccessful) emit(RequestStatus.Success(data = response.body()))
                }
            } catch (throwable: Throwable) {
                emit(RequestStatus.Error(null, throwable.message))
            }
        }.flowOn(Dispatchers.IO)

    override suspend fun limitedApp(id: String, limited: Limited): Flow<RequestStatus> =
        flow {
            try {
                emit(RequestStatus.Loading(null))

                withTimeout(NETWORK_TIMEOUT) {
                    val response = client.onLimit(id, limited)
                    if (response.isSuccessful) emit(RequestStatus.Success(data = response.body()))
                }
            } catch (throwable: Throwable) {
                emit(RequestStatus.Error(null, throwable.message))
            }
        }.flowOn(Dispatchers.IO)
}