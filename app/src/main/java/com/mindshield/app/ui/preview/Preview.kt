package com.mindshield.app.ui.preview

import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.runtime.Composable
import com.mindshield.app.ui.login.LoginScreen
import com.mindshield.app.ui.register.RegisterScreen
import com.mindshield.app.ui.theme.MindShieldTheme

@Preview(showBackground = true, showSystemUi = true, device = "spec:width=411dp,height=891dp")
@Composable
fun LoginScreenPreview() {
    MindShieldTheme {
        LoginScreen(
            onLoginSuccess = {},
            onNavigateToRegister = {},
            onForgotPassword = {}
        )
    }
}

@Preview(showBackground = true, showSystemUi = true, device = "spec:width=411dp,height=891dp", uiMode = 0x21)
@Composable
fun LoginScreenDarkPreview() {
    MindShieldTheme(darkTheme = true) {
        LoginScreen(
            onLoginSuccess = {},
            onNavigateToRegister = {},
            onForgotPassword = {}
        )
    }
}

@Preview(showBackground = true, showSystemUi = true, device = "spec:width=411dp,height=891dp")
@Composable
fun RegisterScreenPreview() {
    MindShieldTheme {
        RegisterScreen(
            onRegisterSuccess = {},
            onNavigateToLogin = {}
        )
    }
}

@Preview(showBackground = true, showSystemUi = true, device = "spec:width=411dp,height=891dp", uiMode = 0x21)
@Composable
fun RegisterScreenDarkPreview() {
    MindShieldTheme(darkTheme = true) {
        RegisterScreen(
            onRegisterSuccess = {},
            onNavigateToLogin = {}
        )
    }
}
