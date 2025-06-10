package com.mindshield.app.ui.components

import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

/**
 * Data class representing a message to be shown in a Snackbar
 */
data class Message(
    val id: Long = System.currentTimeMillis(),
    val message: String,
    val actionLabel: String? = null,
    val withDismissAction: Boolean = false,
    val duration: SnackbarDuration = if (actionLabel == null) SnackbarDuration.Short else SnackbarDuration.Long
)

/**
 * Manages the state and display of Snackbar messages
 */
class SnackbarManager {
    private val messages = mutableStateListOf<Message>()
    private var currentMessage: Message? by mutableStateOf(null)
    
    /**
     * Shows a new message in the Snackbar
     */
    fun showMessage(
        message: String,
        actionLabel: String? = null,
        withDismissAction: Boolean = false,
        duration: SnackbarDuration = if (actionLabel == null) SnackbarDuration.Short else SnackbarDuration.Long
    ) {
        messages.add(
            Message(
                message = message,
                actionLabel = actionLabel,
                withDismissAction = withDismissAction,
                duration = duration
            )
        )
    }
    
    /**
     * Removes the current message
     */
    fun removeCurrentMessage() {
        currentMessage = null
    }
    
    /**
     * Sets up the Snackbar host and starts showing messages
     */
    @Composable
    fun SetupSnackbarHost(
        snackbarHostState: SnackbarHostState,
        coroutineScope: CoroutineScope,
        onAction: (() -> Unit)? = null
    ) {
        // Show the next message when the current one is dismissed
        LaunchedEffect(snackbarHostState) {
            while (true) {
                // Wait for the current message to be dismissed
                snackbarHostState.currentSnackbarData?.dismiss()
                
                // Get the next message if available
                currentMessage = messages.getOrNull(0)?.also { message ->
                    try {
                        val result = snackbarHostState.showSnackbar(
                            message = message.message,
                            actionLabel = message.actionLabel,
                            withDismissAction = message.withDismissAction,
                            duration = message.duration
                        )
                        
                        // Handle action if clicked
                        if (result == SnackbarResult.ActionPerformed) {
                            onAction?.invoke()
                        }
                    } finally {
                        // Remove the message from the queue
                        messages.remove(message)
                    }
                } ?: run {
                    // No more messages, wait for the next one
                    kotlinx.coroutines.withTimeout(Long.MAX_VALUE) {}
                    null
                }
            }
        }
    }
}

/**
 * Creates and remembers a [SnackbarManager]
 */
@Composable
fun rememberSnackbarManager(): SnackbarManager {
    return remember { SnackbarManager() }
}
