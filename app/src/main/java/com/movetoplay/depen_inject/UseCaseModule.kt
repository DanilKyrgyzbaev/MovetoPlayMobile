package com.movetoplay.depen_inject

import com.movetoplay.domain.repository.AuthRepository
import com.movetoplay.domain.use_case.CreateSessionUseCase
import com.movetoplay.domain.use_case.RegularExpressionsUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {

    @Provides
    fun provideCreateSessionUseCase(
        authRepository: AuthRepository
    ) : CreateSessionUseCase{
        return CreateSessionUseCase(
            authRepository,
            RegularExpressionsUseCase()
        )
    }
}