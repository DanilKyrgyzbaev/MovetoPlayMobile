package com.movetoplay.depen_inject

import android.content.Context
import com.movetoplay.App
import com.movetoplay.domain.repository.AuthRepository
import com.movetoplay.domain.repository.ProfileRepository
import com.movetoplay.domain.repository.ProfilesRepository
import com.movetoplay.domain.use_case.AnalysisImageUseCase
import com.movetoplay.domain.use_case.CreateSessionUseCase
import com.movetoplay.domain.use_case.DeterminePoseStarJumpUseCase
import com.movetoplay.domain.use_case.RegularExpressionsUseCase
import com.movetoplay.domain.use_case.select_profile_child.CreateProfileChildUseCase
import com.movetoplay.domain.use_case.select_profile_child.GetListChildUseCase
import com.movetoplay.domain.use_case.select_profile_child.SaveProfileChildUseCase
import com.movetoplay.domain.use_case.select_profile_child.SelectProfileChildUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {

    @Provides
    fun provideCreateSessionUseCase(
        @ApplicationContext appContext: Context,
        authRepository: AuthRepository
    ) : CreateSessionUseCase{
        return CreateSessionUseCase(
            appContext,
            authRepository,
            RegularExpressionsUseCase()
        )
    }

    @Provides
    fun provideAnalysisImageUseCase(): AnalysisImageUseCase {
        return AnalysisImageUseCase(
            DeterminePoseStarJumpUseCase()
        )
    }

    @Provides
    fun provideSelectProfileChildUseCase(
        profileRepository: ProfileRepository,
        profilesRepository: ProfilesRepository,
        createSession: CreateSessionUseCase
    ) : SelectProfileChildUseCase {
        val createProfileChild = CreateProfileChildUseCase(
            profileRepository = profileRepository,
            profilesRepository= profilesRepository,
            createSessionUseCase = createSession
        )
        return  SelectProfileChildUseCase(
            saveProfileChild = SaveProfileChildUseCase(
                profileRepository = profileRepository,
                createProfileChildUseCase = createProfileChild
            ),
            getListChild = GetListChildUseCase(
                profileRepository = profileRepository,
                profilesRepository= profilesRepository,
                createSessionUseCase = createSession
            )
        )
    }

}