package com.movetoplay.presentation.vm.profile_childe_vm

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.movetoplay.domain.model.Exercise
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

@HiltViewModel
class ProfileChildVM @Inject constructor() : ViewModel() {
    private val _availableForDay = mutableStateOf(45)
    val availableForDay: State<Int> get() = _availableForDay
    val flowRemainingTime = flow<Int> {
        emit(25)
    }
    private val _listExerciseForDay = mutableStateListOf<Exercise>()
    val listExerciseForDay: List<Exercise> get() = _listExerciseForDay

    private val _listExerciseRemainingTime = mutableStateListOf<Exercise>()
    val listExerciseFRemainingTime: List<Exercise> get() = _listExerciseRemainingTime
}
