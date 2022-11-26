package com.movetoplay.screens.parent

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.movetoplay.domain.model.ChildInfo
import com.movetoplay.domain.model.DailyExercises
import com.movetoplay.domain.model.user_apps.UserApp
import com.movetoplay.domain.repository.ExercisesRepository
import com.movetoplay.domain.repository.ProfilesRepository
import com.movetoplay.domain.repository.UserAppsRepository
import com.movetoplay.domain.utils.ResultStatus
import com.movetoplay.pref.Pref
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChildInfoViewModel @Inject constructor(
    private val exercisesRepo: ExercisesRepository,
    private val profilesRepository: ProfilesRepository,
    private val userAppsRepository: UserAppsRepository
) :
    ViewModel() {

    val dailyExerciseResult = MutableLiveData<ResultStatus<DailyExercises>>()
    val childInfoResult = MutableLiveData<ResultStatus<ChildInfo>>()
    var userApps = MutableLiveData<ResultStatus<List<UserApp>>>()

    fun getUserApps(id: String) {
        viewModelScope.launch {
            userAppsRepository.getLimitedApps(Pref.userAccessToken, id).collect { appsResponse ->
                when (appsResponse) {
                    is ResultStatus.Loading -> {
                        userApps.value = ResultStatus.Loading()
                    }
                    is ResultStatus.Success -> {
                        launch(Dispatchers.Default) {
                            userApps.postValue(
                                ResultStatus.Success(
                                    sortList(appsResponse.data)
                                )
                            )
                        }
                    }
                    is ResultStatus.Error -> {
                        userApps.value = ResultStatus.Error(appsResponse.error)
                    }
                }
            }
        }
    }

    private fun sortList(list: List<UserApp>?): List<UserApp> {
        val sortedList = list as ArrayList
        sortedList.sortByDescending {
            it.time?:0
        }
        return sortedList
    }

    fun getDailyExercises(profileId: String) {
        dailyExerciseResult.value = ResultStatus.Loading()
        viewModelScope.launch {
            when (val result = exercisesRepo.getDaily(profileId, Pref.userAccessToken)) {
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