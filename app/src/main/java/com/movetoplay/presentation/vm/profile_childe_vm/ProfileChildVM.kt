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
import com.movetoplay.domain.model.Exercise
import com.movetoplay.domain.model.TypeExercise
import com.movetoplay.domain.repository.ExercisesRepository
import com.movetoplay.domain.utils.ResultStatus
import com.movetoplay.model.ErrorResponse
import com.movetoplay.model.Touch
import com.movetoplay.network_api.ApiService
import com.movetoplay.network_api.RetrofitClient
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
class ProfileChildVM @Inject constructor(private val exerciseRepo: ExercisesRepository) :
    ViewModel() {
    private val _availableForDay = mutableStateOf(45)
    val availableForDay: State<Int> get() = _availableForDay
    val flowRemainingTime = flow<Int> {
        emit(25)
    }
    val errorHandle = MutableLiveData<String>()
    var api: ApiService = RetrofitClient().getApi()
    val mutableLiveData = MutableLiveData<Boolean>()

    private val _listExerciseForDay = mutableStateListOf<Exercise>()
    val listExerciseForDay: List<Exercise> get() = _listExerciseForDay

    private val _listExerciseRemainingTime = mutableStateListOf<Exercise>()
    val listExerciseFRemainingTime: List<Exercise> get() = _listExerciseRemainingTime

    val sendTouchResult = MutableLiveData<ResultStatus<Boolean>>()

    fun sendTouch(touch: Touch) {
        api.sendTouch("Bearer ${Pref.userAccessToken}", touch).enqueue(object : Callback<Touch> {
            override fun onResponse(call: Call<Touch>, response: Response<Touch>) {
                if (response.isSuccessful) {
                    response.body()
                    mutableLiveData.value = true
                } else {
                    response.errorBody()
                    val error = response.errorBody().toApiError<ErrorResponse>().message
                    errorHandle.value = error
                    Log.e("ResponseError", error.toString())
                }
            }

            override fun onFailure(call: Call<Touch>, t: Throwable) {
                errorHandle.postValue(t.message)
                Log.e("onFailure", "${t.message}")
                errorHandle.value = t.message
            }
        })
    }

    private inline fun <reified ErrorType> ResponseBody?.toApiError(): ErrorType {
        val errorJson = this?.string()
        Log.e("retrying", "to api error body $errorJson")
        val data = Gson().fromJson(
            errorJson,
            ErrorType::class.java
        )
        Log.e("retrying", "to api error ${Gson().toJson(data)})")
        return data
    }

    fun sendTouch() {
        sendTouchResult.value = ResultStatus.Loading()
        viewModelScope.launch {
            var count = 0
            val poseType = when (Pref.pose) {
                "jumps.csv" -> {
                    count = Pref.jumps.toInt()
                    "jumps"
                }
                "sits.csv" -> {
                    count = Pref.sits.toInt()
                    "squats"
                }
                else -> {
                    count = Pref.push_ups.toInt()
                    "squeezing"
                }
            }
            when (val result = exerciseRepo.postTouch(TouchBody(poseType, count, Pref.unixTime))) {
                is ResultStatus.Loading -> {}
                is ResultStatus.Error -> {
                    sendTouchResult.value = ResultStatus.Error(result.error)
                }
                is ResultStatus.Success -> {
                    sendTouchResult.value = ResultStatus.Success(true)
                }
            }
        }
    }
}
