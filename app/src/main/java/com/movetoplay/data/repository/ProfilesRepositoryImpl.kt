package com.movetoplay.data.repository

import com.movetoplay.data.mapper.toApiError
import com.movetoplay.data.model.ChildBody
import com.movetoplay.data.model.ErrorBody
import com.movetoplay.data.model.LimitSettingsBody
import com.movetoplay.data.model.user_apps.PinBody
import com.movetoplay.domain.model.Child
import com.movetoplay.domain.model.ChildInfo
import com.movetoplay.domain.repository.ProfilesRepository
import com.movetoplay.domain.utils.ResultStatus
import com.movetoplay.network_api.ApiService
import com.movetoplay.pref.Pref
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withTimeout

class ProfilesRepositoryImpl(
    private val api: ApiService
) : ProfilesRepository {
    override suspend fun getListProfileChild(): List<Child> {
        return try {
            val res = api.getChildes("Bearer ${Pref.userAccessToken}")

            if (res.isSuccessful) res.body()!!
            else throw Throwable(res.errorBody().toApiError<ErrorBody>().message)
        } catch (e: Throwable) {
            throw Throwable(e.localizedMessage)
        }
    }

    override suspend fun createProfileChild(child: ChildBody): Child {
        return try {
            val res = api.postChildProfile(
                "Bearer ${Pref.userAccessToken}", child
            )
            if (res.isSuccessful) res.body()!!
            else throw Throwable(res.errorBody().toApiError<ErrorBody>().message)
        } catch (e: Throwable) {
            throw Throwable(e.message)
        }
    }

    override suspend fun getInfo(id: String): ChildInfo {
        return try {
            val res = api.getInfo(
                "Bearer ${Pref.userAccessToken}",
                id
            )
            if (res.isSuccessful) res.body()!!
            else throw Throwable(res.errorBody().toApiError<ErrorBody>().message)
        } catch (e: Throwable) {
            throw Throwable(e.localizedMessage)
        }
    }

    override suspend fun updateLimitations(
        id: String,
        limitSettingsBody: LimitSettingsBody
    ): ResultStatus<Boolean> {
        return try {
            val res = api.updateProfileSettings(
                "Bearer ${Pref.userAccessToken}",
                id,
                limitSettingsBody
            )
            if (res.isSuccessful) ResultStatus.Success(true)
            else ResultStatus.Error(res.errorBody().toApiError<ErrorBody>().message)
        } catch (e: Throwable) {
            ResultStatus.Error(e.message)
        }
    }

    override suspend fun setPinCode(pinBody: PinBody): Boolean {
        return try {
            withTimeout(NETWORK_TIMEOUT) {
                val response = api.setPinCode("Bearer ${Pref.userAccessToken}", pinBody)
                if (response.isSuccessful) true
                else throw Throwable(response.errorBody().toApiError<ErrorBody>().message)
            }
        } catch (throwable: Throwable) {
            throw Throwable(throwable.message)
        }
    }

}