package com.mindshield.app.ui.auth

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.mindshield.app.R
import com.mindshield.app.ui.components.PrimaryButton
import com.mindshield.app.ui.theme.Dimens

@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit,
    onNavigateToRegister: () -> Unit,
    onForgotPassword: () -> Unit,
    viewModel: LoginViewModel = hiltViewModel()
) {
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var passwordVisible by rememberSaveable { mutableStateOf(false) }
    val loginState by viewModel.loginState.collectAsState()
    val context = LocalContext.current

    // Handle login state changes
    LaunchedEffect(loginState) {
        when (loginState) {
            is LoginState.Success -> {
                onLoginSuccess()
            }
            is LoginState.Error -> {
                // Show error message
                val errorMessage = (loginState as LoginState.Error).message
                // You can show a snackbar or toast here
                // ScaffoldHostState.current.showSnackbar(errorMessage)
            }
            else -> { /* Do nothing */ }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = Dimens.ScreenPadding),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // App Logo/Title
        Text(
            text = stringResource(id = R.string.app_name),
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(bottom = Dimens.LargePadding)
        )

        // Email Field
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text(stringResource(id = R.string.hint_email)) },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Email,
                    contentDescription = null
                )
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = Dimens.MediumPadding)
        )

        // Password Field
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text(stringResource(id = R.string.hint_password)) },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Lock,
                    contentDescription = null
                )
            },
            visualTransformation = if (passwordVisible) {
                VisualTransformation.None
            } else {
                PasswordVisualTransformation()
            },
            trailingIcon = {
                val image = if (passwordVisible) {
                    Icons.Filled.Visibility
                } else {
                    Icons.Filled.VisibilityOff
                }
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(
                        imageVector = image,
                        contentDescription = if (passwordVisible) {
                            stringResource(id = R.string.hide_password)
                        } else {
                            stringResource(id = R.string.show_password)
                        }
                    )
                }
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = Dimens.MediumPadding)
        )

        // Forgot Password Link
        TextButton(
            onClick = onForgotPassword,
            modifier = Modifier
                .align(Alignment.End)
                .padding(bottom = Dimens.LargePadding)
        ) {
            Text(stringResource(id = R.string.action_forgot_password))
        }

        // Login Button
        PrimaryButton(
            text = stringResource(id = R.string.action_login),
            onClick = { viewModel.login(email, password) },
            isLoading = loginState is LoginState.Loading,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = Dimens.MediumPadding)
        )

        // Register Link
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(top = Dimens.MediumPadding)
        ) {
            Text(
                text = stringResource(id = R.string.dont_have_account),
                style = MaterialTheme.typography.bodyMedium
            )
            TextButton(
                onClick = onNavigateToRegister
            ) {
                Text(stringResource(id = R.string.action_register))
            }
        }
    }
}
