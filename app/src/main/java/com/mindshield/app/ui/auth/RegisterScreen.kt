package com.mindshield.app.ui.auth

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
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
fun RegisterScreen(
    onRegisterSuccess: () -> Unit,
    onNavigateToLogin: () -> Unit,
    viewModel: RegisterViewModel = hiltViewModel()
) {
    var name by rememberSaveable { mutableStateOf("") }
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var confirmPassword by rememberSaveable { mutableStateOf("") }
    var passwordVisible by rememberSaveable { mutableStateOf(false) }
    var confirmPasswordVisible by rememberSaveable { mutableStateOf(false) }
    val registerState by viewModel.registerState.collectAsState()

    // Handle registration state changes
    LaunchedEffect(registerState) {
        when (registerState) {
            is RegisterState.Success -> {
                onRegisterSuccess()
            }
            is RegisterState.Error -> {
                // Show error message
                val errorMessage = (registerState as RegisterState.Error).message
                // You can show a snackbar or toast here
                // ScaffoldHostState.current.showSnackbar(errorMessage)
            }
            else -> { /* Do nothing */ }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = Dimens.ScreenPadding)
            .padding(vertical = Dimens.LargePadding),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // App Logo/Title
        Text(
            text = stringResource(id = R.string.create_account),
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(bottom = Dimens.LargePadding)
        )

        // Name Field
        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text(stringResource(id = R.string.hint_name)) },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = null
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = Dimens.MediumPadding)
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

        // Confirm Password Field
        OutlinedTextField(
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            label = { Text(stringResource(id = R.string.hint_confirm_password)) },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.LockOutline,
                    contentDescription = null
                )
            },
            visualTransformation = if (confirmPasswordVisible) {
                VisualTransformation.None
            } else {
                PasswordVisualTransformation()
            },
            trailingIcon = {
                val image = if (confirmPasswordVisible) {
                    Icons.Filled.Visibility
                } else {
                    Icons.Filled.VisibilityOff
                }
                IconButton(onClick = { confirmPasswordVisible = !confirmPasswordVisible }) {
                    Icon(
                        imageVector = image,
                        contentDescription = if (confirmPasswordVisible) {
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
                .padding(bottom = Dimens.LargePadding)
        )

        // Register Button
        PrimaryButton(
            text = stringResource(id = R.string.action_register),
            onClick = { viewModel.register(name, email, password, confirmPassword) },
            isLoading = registerState is RegisterState.Loading,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = Dimens.MediumPadding)
        )

        // Login Link
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(top = Dimens.MediumPadding)
        ) {
            Text(
                text = stringResource(id = R.string.already_have_account),
                style = MaterialTheme.typography.bodyMedium
            )
            TextButton(
                onClick = onNavigateToLogin
            ) {
                Text(stringResource(id = R.string.action_login))
            }
        }
    }
}
