package com.movetoplay.domain.use_case.select_profile_child

import com.movetoplay.domain.model.Child
import com.movetoplay.domain.repository.ProfileRepository
import com.movetoplay.domain.repository.ProfilesRepository
import com.movetoplay.domain.use_case.CreateSessionUseCase
import com.movetoplay.domain.utils.RequestStatus
import com.movetoplay.domain.utils.StateProblems

class CreateProfileChildUseCase(
    private val profilesRepository: ProfilesRepository,
    private val profileRepository: ProfileRepository,
    private val createSessionUseCase: CreateSessionUseCase
){
    suspend operator fun invoke(child: Child) : Child {
        when(val answer = profilesRepository.createProfileChild(child)){
            is RequestStatus.Success<*> -> {
                return answer.data as Child
            }
            is RequestStatus.Error<*> -> {
                if(answer.data is StateProblems.NeedRestoreSession){
                    profileRepository.apply {
                        when(createSessionUseCase(email!!,password!!, null)){
                            is RequestStatus.Success<*> ->{
                                return this@CreateProfileChildUseCase.invoke(child)
                            }
                            is RequestStatus.Error<*> -> {

                            }
                            else->{}
                        }
                    }
                }
            }
            else ->{}
        }
        throw Exception("Произошло исключение")
    }
}