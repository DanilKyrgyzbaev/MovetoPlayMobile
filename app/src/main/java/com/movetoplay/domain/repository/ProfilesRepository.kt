package com.movetoplay.domain.repository

import com.movetoplay.data.model.LimitSettingsBody
import com.movetoplay.domain.model.Child
import com.movetoplay.domain.model.ChildInfo
import com.movetoplay.domain.utils.ResultStatus

interface ProfilesRepository {
    suspend fun getListProfileChild(): List<Child>
    suspend fun createProfileChild(child: Child): Child
    suspend fun getInfo(id: String): ChildInfo
    suspend fun updateLimitations(id: String,limitSettingsBody: LimitSettingsBody): ResultStatus<Boolean>
}