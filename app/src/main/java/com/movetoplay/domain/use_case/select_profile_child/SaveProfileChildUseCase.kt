package com.movetoplay.domain.use_case.select_profile_child

import com.movetoplay.domain.model.Child
import com.movetoplay.domain.repository.ProfileRepository

class SaveProfileChildUseCase(
    private val createProfileChildUseCase : CreateProfileChildUseCase,
    private val profileRepository: ProfileRepository
){
    suspend operator fun invoke(child: Child){
        if (child.parentAccountId.isEmpty()){
            profileRepository.child = createProfileChildUseCase(child)
        }else{
            profileRepository.child = child
        }
    }
}