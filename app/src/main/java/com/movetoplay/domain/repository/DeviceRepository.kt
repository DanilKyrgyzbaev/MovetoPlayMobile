package com.movetoplay.domain.repository

import com.movetoplay.data.model.DeviceBody
import com.movetoplay.domain.utils.ResultStatus
import com.movetoplay.domain.model.ChildDevice

interface DeviceRepository {
    suspend fun createDevice(device: DeviceBody):ResultStatus<ChildDevice>
    suspend fun getDevice(id:String):ResultStatus<ChildDevice>
}