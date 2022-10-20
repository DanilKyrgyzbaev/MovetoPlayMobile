package com.movetoplay.domain.repository

import com.movetoplay.data.model.ErrorBody
import com.movetoplay.data.model.user_apps.UserAppsBody
import com.movetoplay.domain.model.user_apps.Limited
import com.movetoplay.domain.model.user_apps.UserApp
import com.movetoplay.domain.utils.ResultStatus
import kotlinx.coroutines.flow.Flow

interface UserAppsRepository {
    suspend fun postLimitedApps(token: String, apps: UserAppsBody): Flow<ResultStatus<ErrorBody>>
    suspend fun getLimitedApps(token: String, id: String): Flow<ResultStatus<List<UserApp>>>
    suspend fun setLimitedApp(id: String, limited: Limited): Flow<ResultStatus<Boolean>>
}