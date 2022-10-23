package com.movetoplay.data.repository

import com.movetoplay.data.model.DeviceBody
import com.movetoplay.domain.repository.DeviceRepository
import com.movetoplay.domain.utils.ResultStatus
import com.movetoplay.domain.model.ChildDevice
import com.movetoplay.network_api.ApiService
import com.movetoplay.pref.Pref
import javax.inject.Inject

class DeviceRepositoryImpl @Inject constructor(private val apiService: ApiService) :
    DeviceRepository {

    override suspend fun createDevice(device: DeviceBody): ResultStatus<ChildDevice> {
        return try {
            val res = apiService.createDevice("Bearer ${Pref.accessToken}", device)

            if (res.isSuccessful) ResultStatus.Success(res.body())
            else ResultStatus.Error(res.errorBody().toString())

        } catch (e: Throwable) {
            ResultStatus.Error(e.localizedMessage)
        }
    }

    override suspend fun getDevice(id: String): ResultStatus<ChildDevice> {
        return try {
            val res = apiService.getDevice("Bearer ${Pref.accessToken}", id)

            if (res.isSuccessful) ResultStatus.Success(res.body())
            else ResultStatus.Error(res.message())

        } catch (e: Throwable) {
            ResultStatus.Error(e.localizedMessage)
        }
    }
}