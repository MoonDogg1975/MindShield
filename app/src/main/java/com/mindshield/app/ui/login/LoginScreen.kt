package com.mindshield.app.ui.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.mindshield.app.R
import com.mindshield.app.ui.theme.MindShieldTheme

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
    val focusManager = LocalFocusManager.current
    val context = LocalContext.current
    val loginState by viewModel.loginState.collectAsState()

    // Handle login state changes
    LaunchedEffect(loginState) {
        when (loginState) {
            is LoginUiState.Success -> {
                onLoginSuccess()
            }
            is LoginUiState.Error -> {
                // Error is shown via the Snackbar in the Scaffold
            }
            else -> { /* Do nothing */ }
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(stringResource(R.string.login_title)) }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Logo
            Image(
                painter = painterResource(id = R.drawable.ic_launcher_foreground),
                contentDescription = stringResource(R.string.app_name),
                modifier = Modifier.size(120.dp)
            )
            
            Spacer(modifier = Modifier.height(32.dp))
            
            // Email field
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text(stringResource(R.string.email)) },
                leadingIcon = {
                    Icon(Icons.Outlined.Email, contentDescription = null)
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = { focusManager.moveFocus(FocusDirection.Down) }
                ),
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Password field
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text(stringResource(R.string.password)) },
                leadingIcon = {
                    Icon(Icons.Outlined.Lock, contentDescription = null)
                },
                trailingIcon = {
                    val image = if (passwordVisible) {
                        Icons.Filled.Visibility
                    } else {
                        Icons.Filled.VisibilityOff
                    }
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(image, contentDescription = null)
                    }
                },
                visualTransformation = if (passwordVisible) {
                    VisualTransformation.None
                } else {
                    PasswordVisualTransformation()
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = { focusManager.clearFocus() }
                ),
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            
            // Forgot password link
            TextButton(
                onClick = onForgotPassword,
                modifier = Modifier.align(Alignment.End)
            ) {
                Text(stringResource(R.string.forgot_password))
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Login button
            Button(
                onClick = { viewModel.login(email, password) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                enabled = loginState !is LoginUiState.Loading
            ) {
                if (loginState is LoginUiState.Loading) {
                    CircularProgressIndicator(
                        color = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.size(24.dp)
                    )
                } else {
                    Text(stringResource(R.string.login))
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Register link
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(vertical = 8.dp)
            ) {
                Text(text = stringResource(R.string.dont_have_account))
                TextButton(
                    onClick = onNavigateToRegister
                ) {
                    Text(stringResource(R.string.register))
                }
            }
        }
    }
    
    // Show error message if there's an error
    if (loginState is LoginUiState.Error) {
        val errorMessage = (loginState as LoginUiState.Error).message
        LaunchedEffect(Unit) {
            // Show snackbar or dialog with error message
            // For now, we'll just log it
            println("Login error: $errorMessage")
        }
    }
}

@Preview(showBackground = true)
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
