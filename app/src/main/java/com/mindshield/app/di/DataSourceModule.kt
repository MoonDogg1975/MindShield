package com.mindshield.app.di

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.mindshield.app.data.source.local.AuthLocalDataSource
import com.mindshield.app.data.source.local.AuthLocalDataSourceImpl
import com.mindshield.app.data.source.remote.AuthApiService
import com.mindshield.app.data.source.remote.AuthRemoteDataSource
import com.mindshield.app.data.source.remote.AuthRemoteDataSourceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataSourceModule {

    @Provides
    @Singleton
    fun provideEncryptedSharedPreferences(
        @ApplicationContext context: Context
    ): EncryptedSharedPreferences {
        val masterKey = MasterKey.Builder(context)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()

        return EncryptedSharedPreferences.create(
            context,
            "mindshield_secure_prefs",
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        ) as EncryptedSharedPreferences
    }

    @Provides
    @Singleton
    fun provideAuthLocalDataSource(
        encryptedPrefs: EncryptedSharedPreferences
    ): AuthLocalDataSource {
        return AuthLocalDataSourceImpl(encryptedPrefs, Dispatchers.IO)
    }

    @Provides
    @Singleton
    fun provideAuthRemoteDataSource(
        authApiService: AuthApiService
    ): AuthRemoteDataSource {
        return AuthRemoteDataSourceImpl(authApiService)
    }
}
