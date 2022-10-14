package com.movetoplay.domain.repository

import com.movetoplay.data.model.user_apps.UserAppsBody
import com.movetoplay.domain.model.user_apps.Limited
import com.movetoplay.domain.utils.RequestStatus
import kotlinx.coroutines.flow.Flow

interface UserAppsRepository {
    suspend fun postLimitedApps(token: String,apps: UserAppsBody): Flow<RequestStatus>
    suspend fun getLimitedApps(token:String) : Flow<RequestStatus>
    suspend fun limitedApp(id:String,limited: Limited): Flow<RequestStatus>
}