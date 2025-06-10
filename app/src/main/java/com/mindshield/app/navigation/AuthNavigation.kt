package com.mindshield.app.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.mindshield.app.ui.login.LoginScreen
import com.mindshield.app.ui.register.RegisterScreen

/**
 * Sealed class representing all screens in the authentication flow
 */
sealed class AuthScreen(val route: String) {
    object Login : AuthScreen("login")
    object Register : AuthScreen("register")
    
    companion object {
        const val AUTH_GRAPH_ROUTE = "auth_graph"
    }
}

/**
 * Composable function that contains the navigation graph for the authentication flow
 */
@Composable
fun AuthNavigation(
    onLoginSuccess: () -> Unit,
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = AuthScreen.Login.route,
        route = AuthScreen.AUTH_GRAPH_ROUTE
    ) {
        // Login Screen
        composable(route = AuthScreen.Login.route) {
            LoginScreen(
                onLoginSuccess = onLoginSuccess,
                onNavigateToRegister = {
                    navController.navigate(AuthScreen.Register.route) {
                        // Prevent multiple copies of the same destination
                        launchSingleTop = true
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
                onRegisterSuccess = {
                    // After successful registration, navigate back to login
                    navController.popBackStack()
                    // TODO: Show success message on login screen
                },
                onNavigateToLogin = {
                    navController.popBackStack()
                }
            )
        }
    }
}
