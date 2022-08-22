package com.movetoplay.domain.repository

import com.movetoplay.domain.model.Child
import com.movetoplay.domain.utils.RequestStatus

interface ProfilesRepository {
    suspend fun getListProfileChild() : List<Child>
    suspend fun createProfileChild(child: Child) : Child
}