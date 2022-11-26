package com.movetoplay.presentation.vm.profile_childe_vm

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.movetoplay.domain.model.DailyExercises
import com.movetoplay.domain.model.Exercise
import com.movetoplay.domain.repository.ExercisesRepository
import com.movetoplay.domain.repository.ProfilesRepository
import com.movetoplay.domain.utils.ResultStatus
import com.movetoplay.model.Touch
import com.movetoplay.network_api.ApiService
import com.movetoplay.network_api.RetrofitClient
import com.movetoplay.pref.AccessibilityPrefs
import com.movetoplay.pref.ExercisesPref
import com.movetoplay.pref.Pref
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class ProfileChildVM @Inject constructor(
    private val profilesRepository: ProfilesRepository,
    private val exercisesRepository: ExercisesRepository
) : ViewModel(), SharedPreferences.OnSharedPreferenceChangeListener {
    private val _availableForDay = mutableStateOf(60 * 60000L)
    val availableForDay: State<Long> get() = _availableForDay

    private val _remainingTime = mutableStateOf(AccessibilityPrefs.remainingTime)
    val remainingTime: State<Long> get() = _remainingTime

    private val _getDailyExercises = mutableStateOf(DailyExercises())
    val dailyExercises: State<DailyExercises> get() = _getDailyExercises
    var flowRemainingTime = flow<Long> {
        emit(AccessibilityPrefs.remainingTime)
    }
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
        AccessibilityPrefs.getSharedPreferences.registerOnSharedPreferenceChangeListener(this)
        getChildInfo()
        getDailyExercises()
    }

    private fun getChildInfo() {
        viewModelScope.launch {
            try {
                profilesRepository.getInfo(Pref.childId).let { info ->
                    info.needSeconds?.let {
                        ExercisesPref.seconds = it
                    }
                    info.extraTime?.let { min ->
                        _availableForDay.value = min * 60000L
                        AccessibilityPrefs.dailyLimit = _availableForDay.value
                        if (Pref.isFirst) {
                            AccessibilityPrefs.remainingTime = AccessibilityPrefs.dailyLimit
                            Pref.isFirst = false
                        }
                    }
                    info.needJumpCount?.let {
                        _defExerciseCount.value["jump"] = it
                        ExercisesPref.jumps = it
                    }
                    info.needSquatsCount?.let {
                        _defExerciseCount.value["squat"] = it
                        ExercisesPref.squats = it
                    }
                    info.needSqueezingCount?.let {
                        _defExerciseCount.value["squeezing"] = it
                        ExercisesPref.squeezing = it
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
                    getDailyExercises()
                }
                is ResultStatus.Error -> {
                    Log.e("childVm", "sendTouch error: " + result.error)
                }
                else -> {}
            }
        }
    }

    fun checkIsExercisesDone(context: Context,tag:String) {
        val daily = dailyExercises.value
        val defExercise = defExerciseCount.value

        if (!ExercisesPref.isExtraTimeUsed) {
            if ((daily.jumps?.count ?: 0) >= (defExercise["jumps"] ?: ExercisesPref.jumps)
                && (daily.squats?.count ?: 0) >= (defExercise["squats"] ?: ExercisesPref.squats)
                && (daily.squeezing?.count ?: 0) >= (defExercise["squeezing"]
                    ?: ExercisesPref.squeezing)
            ) {
                AccessibilityPrefs.remainingTime =
                    AccessibilityPrefs.remainingTime.plus(TimeUnit.SECONDS.toMillis(ExercisesPref.seconds.toLong()))
                ExercisesPref.isExtraTimeUsed = true
                Toast.makeText(context, "Время успешно добавлено", Toast.LENGTH_SHORT).show()
            } else {
               if (tag == "click") Toast.makeText(
                    context,
                    "Выполните все оставшиеся упражнения",
                    Toast.LENGTH_SHORT
                ).show()
            }
        } else {
            if (tag == "click") Toast.makeText(context, "Вы уже воспользовались доп. временем", Toast.LENGTH_SHORT)
                .show()
        }
    }

    private fun getDailyExercises() {
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

    override fun onSharedPreferenceChanged(p0: SharedPreferences?, p1: String?) {
        if (p1 != null && p1 == AccessibilityPrefs.remainingTimeKey) {
            _remainingTime.value = AccessibilityPrefs.remainingTime
        }
    }

    override fun onCleared() {
        super.onCleared()
        AccessibilityPrefs.getSharedPreferences.unregisterOnSharedPreferenceChangeListener(this)
    }
}
