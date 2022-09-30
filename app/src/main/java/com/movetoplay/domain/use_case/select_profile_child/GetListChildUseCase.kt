package com.movetoplay.domain.use_case.select_profile_child

import com.movetoplay.domain.model.Child
import com.movetoplay.domain.repository.ProfileRepository
import com.movetoplay.domain.repository.ProfilesRepository
import com.movetoplay.domain.use_case.session.CreateSessionUseCase
import com.movetoplay.domain.use_case.session.RestoreSessionUseCase
import com.movetoplay.domain.utils.RequestStatus
import com.movetoplay.domain.utils.StateProblems

class GetListChildUseCase(
    private val profilesRepository: ProfilesRepository,
    private val restoreSessionUseCase: RestoreSessionUseCase
){
    suspend operator fun invoke() : List<Child>{
        return try {
            profilesRepository.getListProfileChild()
        }catch (e:StateProblems.NeedRestoreSession){
            if (restoreSessionUseCase()){
                invoke()
            }else throw StateProblems.Contingency
        }
    }
}