package com.mindshield.app.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.VisualTransformation

/**
 * A reusable form field with label, error message, and other common configurations
 */
@Composable
fun FormField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    placeholder: String? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    isError: Boolean = false,
    errorMessage: String? = null,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    singleLine: Boolean = true,
    enabled: Boolean = true,
    readOnly: Boolean = false,
) {
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            label = { Text(label) },
            placeholder = placeholder?.let { { Text(it) } },
            leadingIcon = leadingIcon,
            trailingIcon = trailingIcon,
            isError = isError,
            visualTransformation = visualTransformation,
            keyboardOptions = keyboardOptions,
            singleLine = singleLine,
            enabled = enabled,
            readOnly = readOnly,
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                errorBorderColor = MaterialTheme.colorScheme.error,
                focusedLabelColor = MaterialTheme.colorScheme.primary,
                unfocusedLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                errorLabelColor = MaterialTheme.colorScheme.error,
            )
        )
        
        if (isError && !errorMessage.isNullOrBlank()) {
            Text(
                text = errorMessage,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.labelSmall,
                modifier = Modifier.padding(start = 16.dp, top = 4.dp)
            )
        }
    }
}

/**
 * A password field with show/hide password toggle
 */
@Composable
fun PasswordField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    placeholder: String? = null,
    isError: Boolean = false,
    errorMessage: String? = null,
    enabled: Boolean = true,
) {
    var passwordVisible by remember { mutableStateOf(false) }
    
    FormField(
        value = value,
        onValueChange = onValueChange,
        label = label,
        placeholder = placeholder,
        isError = isError,
        errorMessage = errorMessage,
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
                    contentDescription = if (passwordVisible) "Hide password" else "Show password"
                )
            }
        },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        singleLine = true,
        enabled = enabled,
        modifier = modifier
    )
}

/**
 * An email field with email-specific keyboard options
 */
@Composable
fun EmailField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String = "Email",
    modifier: Modifier = Modifier,
    placeholder: String? = null,
    isError: Boolean = false,
    errorMessage: String? = null,
    enabled: Boolean = true,
) {
    FormField(
        value = value,
        onValueChange = onValueChange,
        label = label,
        placeholder = placeholder,
        leadingIcon = {
            Icon(
                imageVector = Icons.Outlined.Email,
                contentDescription = null
            )
        },
        isError = isError,
        errorMessage = errorMessage,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Email,
            imeAction = ImeAction.Next
        ),
        singleLine = true,
        enabled = enabled,
        modifier = modifier
    )
}
