package com.mindshield.app.ui.register

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
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

@OptIn(ExperimentalComposeUiApi::class)
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
    
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val context = LocalContext.current
    val registerState by viewModel.registerState.collectAsState()

    // Handle registration state changes
    LaunchedEffect(registerState) {
        when (registerState) {
            is RegisterUiState.Success -> {
                onRegisterSuccess()
            }
            is RegisterUiState.Error -> {
                // Error is shown via the Snackbar in the Scaffold
            }
            else -> { /* Do nothing */ }
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(stringResource(R.string.register_title)) },
                navigationIcon = {
                    IconButton(onClick = onNavigateToLogin) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_arrow_back),
                            contentDescription = stringResource(R.string.back)
                        )
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 32.dp)
                .padding(top = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            // Name field
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text(stringResource(R.string.name)) },
                leadingIcon = {
                    Icon(Icons.Outlined.Person, contentDescription = null)
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = { focusManager.moveFocus(FocusDirection.Down) }
                ),
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
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
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = { focusManager.moveFocus(FocusDirection.Down) }
                ),
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Confirm Password field
            OutlinedTextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                label = { Text(stringResource(R.string.confirm_password)) },
                leadingIcon = {
                    Icon(Icons.Outlined.Lock, contentDescription = null)
                },
                trailingIcon = {
                    val image = if (confirmPasswordVisible) {
                        Icons.Filled.Visibility
                    } else {
                        Icons.Filled.VisibilityOff
                    }
                    IconButton(onClick = { confirmPasswordVisible = !confirmPasswordVisible }) {
                        Icon(image, contentDescription = null)
                    }
                },
                visualTransformation = if (confirmPasswordVisible) {
                    VisualTransformation.None
                } else {
                    PasswordVisualTransformation()
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = { 
                        keyboardController?.hide()
                        focusManager.clearFocus()
                    }
                ),
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            
            Spacer(modifier = Modifier.weight(1f))
            
            // Register button
            Button(
                onClick = { 
                    viewModel.register(name, email, password)
                    keyboardController?.hide()
                    focusManager.clearFocus()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .padding(bottom = 24.dp),
                enabled = registerState !is RegisterUiState.Loading &&
                        name.isNotBlank() &&
                        email.isNotBlank() &&
                        password.isNotBlank() &&
                        confirmPassword.isNotBlank()
            ) {
                if (registerState is RegisterUiState.Loading) {
                    CircularProgressIndicator(
                        color = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.size(24.dp)
                    )
                } else {
                    Text(stringResource(R.string.register))
                }
            }
        }
    }
    
    // Show error message if there's an error
    if (registerState is RegisterUiState.Error) {
        val errorMessage = (registerState as RegisterUiState.Error).message
        LaunchedEffect(Unit) {
            // Show snackbar or dialog with error message
            // For now, we'll just log it
            println("Registration error: $errorMessage")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RegisterScreenPreview() {
    MindShieldTheme {
        RegisterScreen(
            onRegisterSuccess = {},
            onNavigateToLogin = {}
        )
    }
}
