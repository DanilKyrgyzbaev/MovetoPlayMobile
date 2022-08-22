package com.movetoplay.depen_inject

import android.content.Context
import com.movetoplay.domain.manager.StateUserAppManager
import com.movetoplay.domain.repository.AuthRepository
import com.movetoplay.domain.repository.ProfileRepository
import com.movetoplay.domain.repository.ProfilesRepository
import com.movetoplay.domain.use_case.HasConnectionUseCase
import com.movetoplay.domain.use_case.session.CreateSessionUseCase
import com.movetoplay.domain.use_case.RegularExpressionsUseCase
import com.movetoplay.domain.use_case.analysis_exercise.*
import com.movetoplay.domain.use_case.select_profile_child.CreateProfileChildUseCase
import com.movetoplay.domain.use_case.select_profile_child.GetListChildUseCase
import com.movetoplay.domain.use_case.select_profile_child.SaveProfileChildUseCase
import com.movetoplay.domain.use_case.select_profile_child.SelectProfileChildUseCase
import com.movetoplay.domain.use_case.session.LoginUseCase
import com.movetoplay.domain.use_case.session.RestoreSessionUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {

    @Provides
    fun provideCreateSessionUseCase(
        @ApplicationContext appContext: Context,
        authRepository: AuthRepository
    ) : CreateSessionUseCase {
        return CreateSessionUseCase(
            appContext,
            authRepository,
            RegularExpressionsUseCase()
        )
    }

    @Provides
    fun provideAnalysisImageUseCase(): AnalysisImageUseCase {
        return AnalysisImageUseCase(
            DeterminePoseUseCase()
        )
    }

    @Provides
    fun provideHasConnectionUseCase(@ApplicationContext appContext: Context,): HasConnectionUseCase{
        return HasConnectionUseCase(appContext)
    }
    @Singleton
    @Provides
    fun provideRestoreSessionUseCase(
        profileRepository: ProfileRepository,
        stateUserAppManager: StateUserAppManager,
        authRepository: AuthRepository,
        hasConnectionUseCase: HasConnectionUseCase
    ) : RestoreSessionUseCase {
        return RestoreSessionUseCase(profileRepository, stateUserAppManager, authRepository)
    }

    @Provides
    fun provideLoginUseCase(
        profileRepository: ProfileRepository,
        stateUserAppManager: StateUserAppManager
    ) : LoginUseCase {
        return LoginUseCase(profileRepository, stateUserAppManager)
    }

    @Provides
    fun provideSelectProfileChildUseCase(
        profilesRepository: ProfilesRepository,
        restoreSessionUseCase: RestoreSessionUseCase,
        profileRepository: ProfileRepository
    ) : SelectProfileChildUseCase {
        val createProfileChild = CreateProfileChildUseCase(
            profilesRepository= profilesRepository,
            restoreSessionUseCase = restoreSessionUseCase
        )
        return  SelectProfileChildUseCase(
            saveProfileChild = SaveProfileChildUseCase(
                profileRepository = profileRepository,
                createProfileChildUseCase = createProfileChild
            ),
            getListChild = GetListChildUseCase(
                restoreSessionUseCase = restoreSessionUseCase,
                profilesRepository= profilesRepository
            )
        )
    }

}