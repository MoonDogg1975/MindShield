package com.mindshield.app

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.mindshield.app.ui.login.LoginScreen
import com.mindshield.app.ui.theme.MindShieldTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class PreviewTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun loginScreen_displaysCorrectly() {
        // Start the app
        composeTestRule.setContent {
            MindShieldTheme {
                LoginScreen(
                    onLoginSuccess = {},
                    onNavigateToRegister = {},
                    onForgotPassword = {}
                )
            }
        }

        // Check that the login button is displayed
        composeTestRule.onNodeWithText("Login").assertIsDisplayed()
        
        // Check that the register link is displayed
        composeTestRule.onNodeWithText("Don't have an account?").assertIsDisplayed()
        
        // Check that the forgot password link is displayed
        composeTestRule.onNodeWithText("Forgot password?").assertIsDisplayed()
    }
}
