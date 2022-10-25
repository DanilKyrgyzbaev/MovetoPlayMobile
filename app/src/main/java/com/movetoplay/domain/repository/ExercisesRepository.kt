package com.movetoplay.domain.repository

import com.movetoplay.data.model.ExerciseResponse
import com.movetoplay.domain.model.DailyExercises
import com.movetoplay.domain.utils.ResultStatus
import com.movetoplay.model.Touch

interface ExercisesRepository {
    suspend fun postTouch(touch: Touch): ResultStatus<ExerciseResponse>
    suspend fun getDaily(childToken: String): ResultStatus<DailyExercises>
}