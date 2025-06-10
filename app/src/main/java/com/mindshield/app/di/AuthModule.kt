package com.mindshield.app.di

import com.mindshield.app.auth.AuthStateManager
import com.mindshield.app.auth.AuthenticationManager
import com.mindshield.app.auth.TokenManager
import com.mindshield.app.auth.AuthInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

/**
 * Hilt module that provides authentication-related dependencies
 */
@Module
@InstallIn(SingletonComponent::class)
object AuthModule {

    @Provides
    @Singleton
    fun provideCoroutineScope(): CoroutineScope {
        return CoroutineScope(SupervisorJob() + Dispatchers.Default)
    }

    @Provides
    @Singleton
    fun provideTokenManager(
        authRepository: com.mindshield.app.data.repository.AuthRepository,
        coroutineScope: CoroutineScope
    ): TokenManager {
        return TokenManager(authRepository, coroutineScope)
    }

    @Provides
    @Singleton
    fun provideAuthStateManager(
        authRepository: com.mindshield.app.data.repository.AuthRepository,
        coroutineScope: CoroutineScope
    ): AuthStateManager {
        return AuthStateManager(authRepository, coroutineScope)
    }

    @Provides
    @Singleton
    fun provideAuthenticationManager(
        authRepository: com.mindshield.app.data.repository.AuthRepository
    ): AuthenticationManager {
        return AuthenticationManager(authRepository)
    }

    @Provides
    @Singleton
    fun provideAuthInterceptor(
        tokenManager: TokenManager,
        networkUtils: com.mindshield.app.utils.NetworkUtils
    ): AuthInterceptor {
        return AuthInterceptor(tokenManager, networkUtils)
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(
        authInterceptor: AuthInterceptor,
        loggingInterceptor: HttpLoggingInterceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .addInterceptor(loggingInterceptor)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(com.mindshield.app.BuildConfig.API_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            level = if (com.mindshield.app.BuildConfig.DEBUG) {
                HttpLoggingInterceptor.Level.BODY
            } else {
                HttpLoggingInterceptor.Level.NONE
            }
        }
    }
}
