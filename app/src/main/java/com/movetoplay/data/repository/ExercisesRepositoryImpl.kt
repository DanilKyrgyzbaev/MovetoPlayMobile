package com.movetoplay.data.repository

import com.movetoplay.data.mapper.toApiError
import com.movetoplay.data.model.ErrorBody
import com.movetoplay.data.model.ExerciseResponse
import com.movetoplay.domain.model.DailyExercises
import com.movetoplay.domain.repository.ExercisesRepository
import com.movetoplay.domain.utils.ResultStatus
import com.movetoplay.model.Touch
import com.movetoplay.network_api.ApiService
import com.movetoplay.pref.Pref
import javax.inject.Inject

class ExercisesRepositoryImpl @Inject constructor(private val api: ApiService) :
    ExercisesRepository {

    override suspend fun postTouch(touch: Touch): ResultStatus<ExerciseResponse> {
        return try {
            val result = api.postTouch("Bearer ${Pref.childToken}", touch)

            if (result.isSuccessful) ResultStatus.Success(result.body())
            else ResultStatus.Error(result.errorBody().toApiError<ErrorBody>().message)

        } catch (e: Exception) {
            return ResultStatus.Error(e.localizedMessage)
        }
    }

    override suspend fun getDaily(childToken: String): ResultStatus<DailyExercises> {
        return try {
            val res = api.getDaily("Bearer $childToken")

            if (res.isSuccessful) ResultStatus.Success(res.body())
            else ResultStatus.Error(res.errorBody().toApiError<ErrorBody>().message)

        } catch (e: Exception) {
            ResultStatus.Error(e.localizedMessage)
        }
    }
}