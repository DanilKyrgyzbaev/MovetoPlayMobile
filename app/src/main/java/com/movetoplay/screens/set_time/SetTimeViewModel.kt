package com.movetoplay.screens.set_time

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.movetoplay.data.model.LimitSettingsBody
import com.movetoplay.domain.repository.ProfilesRepository
import com.movetoplay.domain.utils.ResultStatus
import com.movetoplay.pref.AccessibilityPrefs
import com.movetoplay.pref.ExercisesPref
import com.movetoplay.pref.Pref
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SetTimeViewModel @Inject constructor(private val profilesRepository: ProfilesRepository) :
    ViewModel() {

    val updateResultStatus = MutableLiveData<ResultStatus<Boolean>>()

    fun updateLimitations(time: Int, squats: Int, jumps: Int, squeezing: Int) {
        updateResultStatus.value = ResultStatus.Loading()

        viewModelScope.launch {
            when (val result = profilesRepository.updateLimitations(
                Pref.childId,
                LimitSettingsBody(jumps, time, squats, squeezing)
            )) {
                is ResultStatus.Success -> {
                    AccessibilityPrefs.dailyLimit = time.toLong()
                    ExercisesPref.seconds = time
                    ExercisesPref.jumps = jumps
                    ExercisesPref.squats = squats
                    ExercisesPref.squeezing = squeezing
                    updateResultStatus.value = ResultStatus.Success(true)
                }
                is ResultStatus.Error -> updateResultStatus.value = ResultStatus.Error(result.error)
                else -> {}
            }
        }
    }
}