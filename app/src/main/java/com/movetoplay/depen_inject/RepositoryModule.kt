package com.movetoplay.depen_inject

import android.content.SharedPreferences
import com.movetoplay.data.repository.AuthRepositoryImpl
import com.movetoplay.data.repository.ProfileRepositoryImpl
import com.movetoplay.data.repository.ProfilesRepositoryImpl
import com.movetoplay.domain.repository.AuthRepository
import com.movetoplay.domain.repository.ProfileRepository
import com.movetoplay.domain.repository.ProfilesRepository
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
    fun provideProfileRepositoryImpl(
        sp : SharedPreferences
    ) : ProfileRepository {
        return ProfileRepositoryImpl(sp)
    }

    @Provides
    fun provideAuthRepository(
        client : HttpClient
    ) : AuthRepository {
        return AuthRepositoryImpl(client)
    }
    @Provides
    fun provideProfilesRepository(
        client : HttpClient,
        repository : ProfileRepository,
    ) : ProfilesRepository {
        return ProfilesRepositoryImpl(client,repository)
    }
}