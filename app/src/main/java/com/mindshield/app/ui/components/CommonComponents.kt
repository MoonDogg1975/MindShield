package com.mindshield.app.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.mindshield.app.R
import com.mindshield.app.ui.theme.Dimens
import com.mindshield.app.ui.utils.UiState

@Composable
fun <T> HandleUiState(
    uiState: UiState<T>,
    onLoading: @Composable () -> Unit = { DefaultLoading() },
    onError: @Composable (String) -> Unit = { DefaultError(it) },
    onEmpty: @Composable () -> Unit = { DefaultEmpty() },
    onSuccess: @Composable (T) -> Unit
) {
    when (uiState) {
        is UiState.Loading -> onLoading()
        is UiState.Error -> onError(uiState.message)
        is UiState.Empty -> onEmpty()
        is UiState.Success -> onSuccess(uiState.data)
    }
}

@Composable
fun DefaultLoading(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
fun DefaultError(
    message: String,
    modifier: Modifier = Modifier,
    onRetry: (() -> Unit)? = null
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(Dimens.paddingLarge),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = message,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.error,
            textAlign = TextAlign.Center
        )
        
        if (onRetry != null) {
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = onRetry) {
                Text("Retry")
            }
        }
    }
}

@Composable
fun DefaultEmpty(
    message: String = "No data available",
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(Dimens.paddingLarge),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = message,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun DefaultDivider(
    modifier: Modifier = Modifier
) {
    Divider(
        modifier = modifier,
        color = MaterialTheme.colorScheme.outlineVariant,
        thickness = 1.dp
    )
}
