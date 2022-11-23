package com.movetoplay.screens.parent

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.movetoplay.domain.model.ChildInfo
import com.movetoplay.domain.model.DailyExercises
import com.movetoplay.domain.repository.AuthRepository
import com.movetoplay.domain.repository.DeviceRepository
import com.movetoplay.domain.repository.ExercisesRepository
import com.movetoplay.domain.repository.ProfilesRepository
import com.movetoplay.domain.utils.ResultStatus
import com.movetoplay.pref.Pref
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainParentViewModel @Inject constructor(
    private val exercisesRepo: ExercisesRepository,
    private val authRepository: AuthRepository,
    private val deviceRepository: DeviceRepository,
    private val profilesRepository: ProfilesRepository
) :
    ViewModel() {

    val dailyExerciseResult = MutableLiveData<ResultStatus<DailyExercises>>()
    val childInfoResult = MutableLiveData<ResultStatus<ChildInfo>>()

//    fun checkDailyExercise() {
//        if (Pref.childToken != "") {
//            getDailyExercises()
//        } else {
//            authorizeProfile()
//        }
//    }

//    private fun authorizeProfile() {
//        viewModelScope.launch {
//            if (Pref.deviceId != "") {
//                when (val result = authRepository.authorizeProfile(Pref.childId, Pref.deviceId)) {
//                    is ResultStatus.Success -> {
//                        result.data?.let {
//                            Pref.childToken = it.accessToken
//                        }
//                        getDailyExercises()
//                    }
//                    is ResultStatus.Error -> {
//                        dailyExerciseResult.value = ResultStatus.Error(result.error)
//                    }
//                    else -> {}
//                }
//            } else getDeviceByMac()
//        }
//    }

//    private fun getDeviceByMac() {
//        viewModelScope.launch {
//            val result = deviceRepository.getDeviceByMacAddress(Pref.childId, getMacAddress())
//            if (result is ResultStatus.Success) {
//                result.data?.id?.let { Pref.deviceId = it }
//                authorizeProfile()
//            } else if (result is ResultStatus.Error) dailyExerciseResult.value =
//                ResultStatus.Error("Сделайте вход с устройства ребенка!")
//        }
//    }

     fun getDailyExercises(profileId: String) {
        dailyExerciseResult.value = ResultStatus.Loading()
        viewModelScope.launch {
            when (val result = exercisesRepo.getDaily(profileId,Pref.userAccessToken)) {
                is ResultStatus.Success -> {
                    dailyExerciseResult.value = ResultStatus.Success(result.data)
                }
                is ResultStatus.Error -> {
                    dailyExerciseResult.value = ResultStatus.Error(result.error)
                }
                else -> {}
            }
        }
    }

    fun getChildInfo() {
        childInfoResult.value = ResultStatus.Loading()
        viewModelScope.launch {
            try {
                childInfoResult.value =
                    ResultStatus.Success(profilesRepository.getInfo(Pref.childId))
            } catch (e: Throwable) {
                childInfoResult.value = ResultStatus.Error(e.message)
            }
        }
    }
}