package com.movetoplay.depen_inject

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.cache.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.resources.*
import io.ktor.serialization.kotlinx.json.*
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    private const val urlServer = ""
    private const val keySharedPreferences = "keySharedPreferences"
    @Provides
    @Singleton
    fun provideHttpClient() : HttpClient {
        return HttpClient(CIO){
            install(Resources)
            install(HttpRequestRetry) {
                retryOnServerErrors(maxRetries = 5)
                exponentialDelay()
            }
            install(HttpCache)
            install(ContentNegotiation) {
                json()
            }
            expectSuccess = true
            defaultRequest{
                url(urlServer)
            }
        }
    }
    @Provides
    @Singleton
    fun provideSharedPreferences(app: Application) : SharedPreferences {
        return app.getSharedPreferences(keySharedPreferences, Context.MODE_PRIVATE)
    }
}