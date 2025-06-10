package com.mindshield.app.data.repository

import com.mindshield.app.data.model.AuthResponse
import com.mindshield.app.data.model.User
import com.mindshield.app.utils.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FakeAuthRepository @Inject constructor() : AuthRepository {
    private val _currentUser = mutableStateOf<User?>(null)
    private val _isLoggedIn = mutableStateOf(false)
    
    override suspend fun login(email: String, password: String): Result<AuthResponse> {
        return if (email.isNotBlank() && password.isNotBlank()) {
            val user = User(
                id = "1",
                email = email,
                name = "Test User",
                createdAt = System.currentTimeMillis().toString(),
                updatedAt = System.currentTimeMillis().toString()
            )
            _currentUser.value = user
            _isLoggedIn.value = true
            
            Result.Success(
                AuthResponse(
                    accessToken = "fake_access_token",
                    refreshToken = "fake_refresh_token",
                    user = user
                )
            )
        } else {
            Result.Error(Exception("Invalid credentials"))
        }
    }
    
    override suspend fun register(name: String, email: String, password: String): Result<AuthResponse> {
        return if (name.isNotBlank() && email.isNotBlank() && password.isNotBlank()) {
            val user = User(
                id = "1",
                email = email,
                name = name,
                createdAt = System.currentTimeMillis().toString(),
                updatedAt = System.currentTimeMillis().toString()
            )
            _currentUser.value = user
            _isLoggedIn.value = true
            
            Result.Success(
                AuthResponse(
                    accessToken = "fake_access_token",
                    refreshToken = "fake_refresh_token",
                    user = user
                )
            )
        } else {
            Result.Error(Exception("Registration failed"))
        }
    }
    
    override suspend fun logout(): Result<Unit> {
        _currentUser.value = null
        _isLoggedIn.value = false
        return Result.Success(Unit)
    }
    
    override suspend fun getCurrentUser(): Result<User?> {
        return Result.Success(_currentUser.value)
    }
    
    override fun observeAuthState(): Flow<Boolean> {
        return flow {
            emit(_isLoggedIn.value)
        }
    }
    
    override suspend fun isLoggedIn(): Boolean {
        return _isLoggedIn.value
    }
    
    override suspend fun saveAuthResponse(authResponse: AuthResponse) {
        _currentUser.value = authResponse.user
        _isLoggedIn.value = true
    }
    
    override suspend fun clearAuthData() {
        _currentUser.value = null
        _isLoggedIn.value = false
    }
    
    // For testing purposes
    fun setLoggedInUser(user: User?) {
        _currentUser.value = user
        _isLoggedIn.value = user != null
    }
}
