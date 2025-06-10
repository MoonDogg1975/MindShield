package com.mindshield.app.di

import com.mindshield.app.data.repository.AuthRepository
import com.mindshield.app.data.repository.AuthRepositoryImpl
import com.mindshield.app.data.source.local.AuthLocalDataSource
import com.mindshield.app.data.source.remote.AuthRemoteDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Dagger Hilt module that provides repository dependencies
 */
@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    /**
     * Provides the [AuthRepository] implementation
     * @param localDataSource The local data source for authentication
     * @param remoteDataSource The remote data source for authentication
     * @return An instance of [AuthRepositoryImpl]
     */
    @Provides
    @Singleton
    fun provideAuthRepository(
        localDataSource: AuthLocalDataSource,
        remoteDataSource: AuthRemoteDataSource
    ): AuthRepository {
        return AuthRepositoryImpl(localDataSource, remoteDataSource)
    }
}
