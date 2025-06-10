package com.mindshield.app.di

import com.mindshield.app.auth.AuthStateManager
import com.mindshield.app.auth.FakeAuthStateManager
import com.mindshield.app.data.repository.AuthRepository
import com.mindshield.app.data.repository.FakeAuthRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [RepositoryModule::class]
)
abstract class PreviewRepositoryModule {
    @Binds
    @Singleton
    abstract fun bindAuthRepository(fakeAuthRepository: FakeAuthRepository): AuthRepository
}

@Module
@TestInstallIn(
    components = [SingletonComponent::class]
)
abstract class PreviewAuthModule {
    @Binds
    @Singleton
    abstract fun bindAuthStateManager(fakeAuthStateManager: FakeAuthStateManager): AuthStateManager
}
