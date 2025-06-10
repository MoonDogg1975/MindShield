package com.mindshield.app.auth

import com.mindshield.app.data.model.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FakeAuthStateManager @Inject constructor(
    private val coroutineScope: CoroutineScope
) {
    private val _authState = MutableStateFlow<AuthState>(AuthState.Initial)
    val authState: StateFlow<AuthState> = _authState.asStateFlow()
    
    fun setLoggedIn(user: User) {
        _authState.value = AuthState.Authenticated(user)
    }
    
    fun setLoggedOut() {
        _authState.value = AuthState.Unauthenticated
    }
    
    fun setLoading() {
        _authState.value = AuthState.Loading
    }
    
    fun setError(message: String) {
        _authState.value = AuthState.Error(message)
    }
}
