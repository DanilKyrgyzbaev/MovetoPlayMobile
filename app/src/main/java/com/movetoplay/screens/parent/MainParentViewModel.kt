package com.movetoplay.screens.parent

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.movetoplay.domain.model.DailyExercises
import com.movetoplay.domain.repository.AuthRepository
import com.movetoplay.domain.repository.DeviceRepository
import com.movetoplay.domain.repository.ExercisesRepository
import com.movetoplay.domain.utils.ResultStatus
import com.movetoplay.pref.Pref
import com.movetoplay.util.getMacAddress
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainParentViewModel @Inject constructor(
    private val exercisesRepo: ExercisesRepository,
    private val authRepository: AuthRepository,
    private val deviceRepository: DeviceRepository
) :
    ViewModel() {

    val dailyExerciseResult = MutableLiveData<ResultStatus<DailyExercises>>()

    fun checkDailyExercise() {
        if (Pref.childToken != "") {
            getDailyExercises()
        } else {
            authorizeProfile()
        }
    }

    private fun authorizeProfile() {
        viewModelScope.launch {
            if (Pref.deviceId != "") {
                when (val result = authRepository.authorizeProfile(Pref.childId, Pref.deviceId)) {
                    is ResultStatus.Success -> {
                        result.data?.let {
                            Pref.childToken = it.accessToken
                        }
                        getDailyExercises()
                    }
                    is ResultStatus.Error -> {
                        dailyExerciseResult.value = ResultStatus.Error(result.error)
                    }
                    else -> {}
                }
            } else getDeviceByMac()
        }
    }

    private fun getDeviceByMac() {
        viewModelScope.launch {
            val result = deviceRepository.getDeviceByMacAddress(Pref.childId, getMacAddress())
            if (result is ResultStatus.Success) {
                result.data?.id?.let { Pref.deviceId = it }
                authorizeProfile()
            } else if (result is ResultStatus.Error) dailyExerciseResult.value =
                ResultStatus.Error(result.error)
        }
    }

    private fun getDailyExercises() {
        dailyExerciseResult.value = ResultStatus.Loading()
        viewModelScope.launch {
            when (val result = exercisesRepo.getDaily(Pref.childToken)) {
                is ResultStatus.Success -> {
                    dailyExerciseResult.value = ResultStatus.Success(result.data)
                }
                is ResultStatus.Error -> {
                    dailyExerciseResult.value = ResultStatus.Error(result.error)
                }
                is ResultStatus.Loading -> {}
            }
        }
    }
}