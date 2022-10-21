package com.movetoplay.depen_inject

import com.movetoplay.data.repository.DeviceRepositoryImpl
import com.movetoplay.data.repository.UserAppsRepositoryImpl
import com.movetoplay.domain.repository.DeviceRepository
import com.movetoplay.domain.repository.UserAppsRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RemoteRepositoryModule {

    @Binds
    abstract fun provideLimitedRepository(
        repositoryImpl: UserAppsRepositoryImpl,
    ): UserAppsRepository

    @Binds
    abstract fun provideDeviceRepository(
        repoImpl: DeviceRepositoryImpl
    ): DeviceRepository
}