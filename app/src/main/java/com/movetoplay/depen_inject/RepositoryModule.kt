package com.movetoplay.depen_inject

import android.content.SharedPreferences
import com.movetoplay.data.repository.AuthRepositoryImpl
import com.movetoplay.data.repository.TokenRepositoryImpl
import com.movetoplay.domain.repository.AuthRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.*
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Singleton
    @Provides
    fun provideTokenRepositoryImpl(
        sp : SharedPreferences
    ) : TokenRepositoryImpl {
        return TokenRepositoryImpl(sp)
    }

    @Provides
    fun provideAuthRepository(
        repository : TokenRepositoryImpl,
        client : HttpClient
    ) : AuthRepository {
        return AuthRepositoryImpl(repository,client)
    }
}