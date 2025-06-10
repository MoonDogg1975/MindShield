package com.mindshield.app.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.mindshield.app.ui.theme.RedPrimary

@Composable
fun PrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    isLoading: Boolean = false
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp),
        enabled = enabled && !isLoading,
        colors = ButtonDefaults.buttonColors(
            containerColor = RedPrimary,
            disabledContainerColor = RedPrimary.copy(alpha = 0.5f)
        )
    ) {
        if (isLoading) {
            // Add a loading indicator here if needed
            Text("Loading...")
        } else {
            Text(
                text = text,
                style = MaterialTheme.typography.labelLarge,
                color = Color.White
            )
        }
    }
}
