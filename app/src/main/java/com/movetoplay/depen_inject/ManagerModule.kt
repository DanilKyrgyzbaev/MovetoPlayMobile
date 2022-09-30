package com.movetoplay.depen_inject

import com.movetoplay.domain.manager.StateUserAppManager
import com.movetoplay.domain.repository.ProfileRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ManagerModule {
    @Provides
    @Singleton
    fun provideStateUserAppManager(
        profileRepository: ProfileRepository
    ) : StateUserAppManager {
        return StateUserAppManager(profileRepository)
    }
}