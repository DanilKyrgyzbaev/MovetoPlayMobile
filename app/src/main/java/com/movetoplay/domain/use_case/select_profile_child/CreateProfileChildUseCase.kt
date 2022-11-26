package com.movetoplay.domain.use_case.select_profile_child

import com.movetoplay.data.model.ChildBody
import com.movetoplay.domain.model.Child
import com.movetoplay.domain.repository.ProfilesRepository
import com.movetoplay.domain.use_case.session.RestoreSessionUseCase
import com.movetoplay.domain.utils.StateProblems

class CreateProfileChildUseCase(
    private val profilesRepository: ProfilesRepository,
    private val restoreSessionUseCase: RestoreSessionUseCase
){
    suspend operator fun invoke(child: ChildBody) : Child {
        return try {
            profilesRepository.createProfileChild(child)
        }catch (e:StateProblems.NeedRestoreSession){
            if (restoreSessionUseCase()){
                invoke(child)
            }else throw StateProblems.Contingency
        }
    }
}