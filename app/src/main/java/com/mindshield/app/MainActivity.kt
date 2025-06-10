package com.mindshield.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.mindshield.app.auth.AuthState
import com.mindshield.app.auth.AuthStateManager
import com.mindshield.app.navigation.AuthNavigation
import com.mindshield.app.navigation.MainNavGraph
import com.mindshield.app.ui.theme.MindShieldTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    
    @Inject
    lateinit var authStateManager: AuthStateManager
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        setContent {
            MindShieldApp(
                authStateManager = authStateManager
            )
        }
    }
}

@Composable
fun MindShieldApp(
    authStateManager: AuthStateManager,
    modifier: Modifier = Modifier
) {
    MindShieldTheme {
        // A surface container using the 'background' color from the theme
        Surface(
            modifier = modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            // Observe authentication state
            val authState by authStateManager.authState.collectAsState()
            
            // Show appropriate UI based on authentication state
            when (val state = authState) {
                is AuthState.Authenticated -> {
                    // Show main app screen
                    MainNavGraph()
                }
                else -> {
                    // Show authentication flow
                    AuthNavigation(
                        onLoginSuccess = {
                            // The auth state will be updated automatically
                            // by the AuthStateManager
                        }
                    )
                }
            }
        }
    }
}
