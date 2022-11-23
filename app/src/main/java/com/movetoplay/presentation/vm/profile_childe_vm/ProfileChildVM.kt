package com.movetoplay.presentation.vm.profile_childe_vm

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.movetoplay.data.model.TouchBody
import com.movetoplay.domain.model.DailyExercises
import com.movetoplay.domain.model.Exercise
import com.movetoplay.domain.repository.ExercisesRepository
import com.movetoplay.domain.repository.ProfilesRepository
import com.movetoplay.domain.utils.ResultStatus
import com.movetoplay.model.ErrorResponse
import com.movetoplay.model.Touch
import com.movetoplay.network_api.ApiService
import com.movetoplay.network_api.RetrofitClient
import com.movetoplay.pref.AccessibilityPrefs
import com.movetoplay.pref.ExercisesPref
import com.movetoplay.pref.Pref
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class ProfileChildVM @Inject constructor(
    private val profilesRepository: ProfilesRepository,
    private val exercisesRepository: ExercisesRepository
) : ViewModel() {
    private val _availableForDay = mutableStateOf(60 * 60000)
    val availableForDay: State<Int> get() = _availableForDay
    private val _getDailyExercises = mutableStateOf(DailyExercises())
    val dailyExercises: State<DailyExercises> get() = _getDailyExercises
    val flowRemainingTime = flow<Int> {
        emit(25 * 60000)
    }
    val errorHandle = MutableLiveData<String>()
    var api: ApiService = RetrofitClient().getApi()
    val mutableLiveData = MutableLiveData<Boolean>()

    private val _listExerciseForDay = mutableStateListOf<Exercise>()
    val listExerciseForDay: List<Exercise> get() = _listExerciseForDay

    private val _listExerciseRemainingTime = mutableStateListOf<Exercise>()
    val listExerciseFRemainingTime: List<Exercise> get() = _listExerciseRemainingTime

    private val _defExerciseCount = mutableStateOf(
        hashMapOf(
            "jump" to ExercisesPref.jumps,
            "squat" to ExercisesPref.squats,
            "squeezing" to ExercisesPref.squeezing
        )
    )
    val defExerciseCount: State<HashMap<String, Int>> get() = _defExerciseCount

    init {
        getChildInfo()
    }

    private fun getChildInfo() {
        viewModelScope.launch {
            try {
                profilesRepository.getInfo(Pref.childId).let { info ->
                    info.needSeconds?.let { min ->
                        _availableForDay.value = min.toInt() * 60000
                        AccessibilityPrefs.dailyLimit = _availableForDay.value.toLong()
                    }
                    info.needJumpCount?.let {
                        _defExerciseCount.value["jump"] = it
                    }
                    info.needSquatsCount?.let {
                        _defExerciseCount.value["squat"] = it
                    }
                    info.needSqueezingCount?.let {
                        _defExerciseCount.value["squeezing"] = it
                    }
                }
            } catch (e: Throwable) {
                Log.e("childVm", "getChildInfo error: " + e.message)
            }
        }
    }

    fun sendTouch(touch: Touch) {
        viewModelScope.launch {
            when (val result = exercisesRepository.postTouch(touch)) {
                is ResultStatus.Success -> {
                    Log.e("childVm", "sendTouch success: " + result.data)
                }
                is ResultStatus.Error -> {
                    Log.e("childVm", "sendTouch error: " + result.error)
                }
                else -> {}
            }
        }
    }

    fun getDailyExercises() {
        viewModelScope.launch {
            when (val result = exercisesRepository.getDaily(Pref.childId, Pref.childToken)) {
                is ResultStatus.Success -> {
                    result.data?.let { _getDailyExercises.value = it }
                }
                is ResultStatus.Error -> {
                    Log.e("childVm", "sendTouch error: " + result.error)
                }
                else -> {}
            }
        }
    }
//
//    fun sendTouch(touch: Touch) {
//        api.sendTouch("Bearer ${Pref.userAccessToken}", touch).enqueue(object : Callback<Touch> {
//            override fun onResponse(call: Call<Touch>, response: Response<Touch>) {
//                if (response.isSuccessful) {
//                    response.body()
//                    mutableLiveData.value = true
//                } else {
//                    response.errorBody()
//                    val error = response.errorBody().toApiError<ErrorResponse>().message
//                    errorHandle.value = error
//                    Log.e("ResponseError", error.toString())
//                }
//            }
//
//            override fun onFailure(call: Call<Touch>, t: Throwable) {
//                errorHandle.postValue(t.message)
//                Log.e("onFailure", "${t.message}")
//                errorHandle.value = t.message
//            }
//        })
//    }
//    private inline fun <reified ErrorType> ResponseBody?.toApiError(): ErrorType {
//        val errorJson = this?.string()
//        Log.e("retrying", "to api error body $errorJson")
//        val data = Gson().fromJson(
//            errorJson,
//            ErrorType::class.java
//        )
//        Log.e("retrying", "to api error ${Gson().toJson(data)})")
//        return data
//    }
//

}
