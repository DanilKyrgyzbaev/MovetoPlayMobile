package com.movetoplay.screens.create_child_profile

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.movetoplay.data.model.DeviceBody
import com.movetoplay.domain.model.Child
import com.movetoplay.domain.model.Gender
import com.movetoplay.domain.repository.AuthRepository
import com.movetoplay.domain.repository.DeviceRepository
import com.movetoplay.domain.repository.ProfilesRepository
import com.movetoplay.domain.utils.ResultStatus
import com.movetoplay.pref.Pref
import com.movetoplay.util.ValidationUtil.isValidAge
import com.movetoplay.util.ValidationUtil.isValidName
import com.movetoplay.util.getDeviceName
import com.movetoplay.util.getMacAddress
import com.movetoplay.util.isValidDeviceName
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SetupProfileViewModel @Inject constructor(
    private val profileRepository: ProfilesRepository,
    private val authRepository: AuthRepository,
    private val deviceRepository: DeviceRepository
) : ViewModel() {

    val createResultStatus = MutableLiveData<ResultStatus<Child>>()
    val getChildesResultStatus = MutableLiveData<ResultStatus<List<Child>>>()
    val syncProfileStatus = MutableLiveData<ResultStatus<Boolean>>()

    fun sendProfileChild(
        fullName: String,
        age: String,
        gender: Gender,
        isSport: Boolean,
        cxt: Context
    ) {
        if (isValidName(cxt, fullName) && isValidAge(cxt, age)) {
            viewModelScope.launch {
                try {
                    createResultStatus.value = ResultStatus.Loading()
                    createResultStatus.value =
                        ResultStatus.Success(
                            profileRepository.createProfileChild(
                                Child(
                                    fullName,
                                    gender,
                                    age.toInt(),
                                    isSport
                                )
                            )
                        )
                } catch (e: Throwable) {
                    createResultStatus.value = ResultStatus.Error(e.message)
                }
            }
        }
    }

    fun getChildesList() {
        viewModelScope.launch {
            try {
                getChildesResultStatus.value = ResultStatus.Loading()
                getChildesResultStatus.value =
                    ResultStatus.Success(profileRepository.getListProfileChild())
            } catch (e: Throwable) {
                getChildesResultStatus.value = ResultStatus.Error(e.localizedMessage)
            }
        }
    }

    fun syncProfile() {
        syncProfileStatus.value = ResultStatus.Loading()

        viewModelScope.launch {
            when (val it =
                deviceRepository.getDeviceByMacAddress(
                    Pref.childId,
                    getMacAddress()
                )) {
                is ResultStatus.Loading -> {}
                is ResultStatus.Error -> {
                    Log.e("authorize", "createDevice ERROR: " + it.error)
                    syncProfileStatus.value = ResultStatus.Error(it.error)
                }
                is ResultStatus.Success -> {
                    Log.e("authorize", "createDevice SUCCESS" + it.data)
                    Pref.deviceId = it.data?.id.toString()
                    authorizeProfile()
                }
            }
        }
    }

    private fun authorizeProfile() {
        viewModelScope.launch {
            when (val authorize =
                authRepository.authorizeProfile(Pref.childId, Pref.deviceId)) {
                is ResultStatus.Loading -> {}
                is ResultStatus.Success -> {
                    Log.e("authorize", "authorizeUser: SUCCESS" + authorize.data)
                    authorize.data?.accessToken?.let {
                        Pref.childToken = it
                    }
                    syncProfileStatus.value = ResultStatus.Success(true)
                }
                is ResultStatus.Error -> {
                    Log.e("authorize", "authorizeUser ERROR: " + authorize.error)
                    syncProfileStatus.value = ResultStatus.Error(authorize.error)
                }
            }
        }
    }

}