package com.mindshield.app.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.mindshield.app.ui.auth.LoginScreen
import com.mindshield.app.ui.auth.RegisterScreen

/**
 * Navigation graph for authentication flow
 */
@Composable
fun AuthNavGraph(
    navController: NavHostController = rememberNavController(),
    onAuthSuccess: () -> Unit,
    startDestination: String = AuthScreen.Login.route
) {
    val authNavController = remember { navController }

    NavHost(
        navController = authNavController,
        startDestination = startDestination
    ) {
        // Login Screen
        composable(route = AuthScreen.Login.route) {
            LoginScreen(
                onLoginSuccess = onAuthSuccess,
                onNavigateToRegister = {
                    authNavController.navigate(AuthScreen.Register.route) {
                        popUpTo(AuthScreen.Login.route) {
                            inclusive = false
                        }
                    }
                },
                onForgotPassword = {
                    // TODO: Implement forgot password navigation
                }
            )
        }

        // Register Screen
        composable(route = AuthScreen.Register.route) {
            RegisterScreen(
                onRegisterSuccess = onAuthSuccess,
                onNavigateToLogin = {
                    authNavController.navigate(AuthScreen.Login.route) {
                        popUpTo(AuthScreen.Register.route) {
                            inclusive = true
                        }
                    }
                }
            )
        }
    }
}

/**
 * Sealed class representing authentication screens
 */
sealed class AuthScreen(val route: String) {
    object Login : AuthScreen("login")
    object Register : AuthScreen("register")
}
