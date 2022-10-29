package com.movetoplay.data.repository

import com.movetoplay.data.mapper.toApiError
import com.movetoplay.data.model.ErrorBody
import com.movetoplay.domain.model.Child
import com.movetoplay.domain.repository.ProfilesRepository
import com.movetoplay.domain.model.CreateProfile
import com.movetoplay.network_api.ApiService
import com.movetoplay.pref.Pref

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

    override suspend fun createProfileChild(child: Child): Child {
        return try {
            val res = api.postChildProfile(
                "Bearer ${Pref.userAccessToken}", CreateProfile(
                    child.fullName, child.age, child.sex, child.isEngagedSports
                )
            )
            if (res.isSuccessful) res.body()!!
            else throw Throwable(res.errorBody().toApiError<ErrorBody>().message)
        } catch (e: Throwable) {
            throw Throwable(e.localizedMessage)
        }
    }

}