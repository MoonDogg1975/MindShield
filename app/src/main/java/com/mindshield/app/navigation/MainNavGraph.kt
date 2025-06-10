package com.mindshield.app.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.mindshield.app.ui.auth.AuthNavGraph
import com.mindshield.app.ui.splash.SplashScreen
import com.mindshield.app.ui.splash.SplashViewModel

/**
 * Main navigation graph for the application
 */
@Composable
fun MainNavGraph(
    navController: NavHostController = rememberNavController(),
    startDestination: String = Screen.Splash.route
) {
    val mainNavController = remember { navController }
    var startRoute by remember { mutableStateOf(startDestination) }

    NavHost(
        navController = mainNavController,
        startDestination = startRoute
    ) {
        // Splash Screen
        composable(route = Screen.Splash.route) {
            val viewModel: SplashViewModel = hiltViewModel()
            
            SplashScreen(
                onNavigateToAuth = {
                    startRoute = Screen.Auth.route
                    mainNavController.navigate(Screen.Auth.route) {
                        popUpTo(Screen.Splash.route) {
                            inclusive = true
                        }
                    }
                },
                onNavigateToHome = {
                    startRoute = Screen.Main.route
                    mainNavController.navigate(Screen.Main.route) {
                        popUpTo(Screen.Splash.route) {
                            inclusive = true
                        }
                    }
                }
            )
        }

        // Auth Navigation Graph
        composable(route = Screen.Auth.route) {
            AuthNavGraph(
                onAuthSuccess = {
                    mainNavController.navigate(Screen.Main.route) {
                        popUpTo(Screen.Auth.route) {
                            inclusive = true
                        }
                    }
                }
            )
        }

        // Main App Navigation Graph
        composable(route = Screen.Main.route) {
            // TODO: Implement main app navigation
            // MainScreen()
        }
    }
}

/**
 * Sealed class representing main app screens
 */
sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object Auth : Screen("auth")
    object Main : Screen("main")
}
