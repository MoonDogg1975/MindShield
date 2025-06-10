package com.mindshield.app.ui.splash

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.mindshield.app.R
import com.mindshield.app.ui.theme.Dimens

@Composable
fun SplashScreen(
    onNavigateToAuth: () -> Unit,
    onNavigateToHome: () -> Unit,
    viewModel: SplashViewModel = hiltViewModel()
) {
    // Check if user is authenticated
    LaunchedEffect(key1 = true) {
        viewModel.checkAuthStatus(
            onAuthenticated = { onNavigateToHome() },
            onUnauthenticated = { onNavigateToAuth() }
        )
    }

    // Splash Screen UI
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(Dimens.LargePadding),
        horizontalAlignment = Alignment.Center,
        verticalArrangement = Arrangement.Center
    ) {
        // App Logo/Icon
        Box(
            modifier = Modifier
                .size(120.dp)
                .padding(bottom = Dimens.MediumPadding)
        ) {
            // TODO: Add your app logo
            // Image(painter = painterResource(id = R.drawable.app_logo), contentDescription = null)
        }
        
        // App Name
        Text(
            text = stringResource(id = R.string.app_name),
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.primary
        )
        
        // Tagline (optional)
        Text(
            text = stringResource(id = R.string.app_tagline),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(top = Dimens.SmallPadding)
        )
    }
}

@Composable
fun SplashScreenPreview() {
    SplashScreen(
        onNavigateToAuth = {},
        onNavigateToHome = {}
    )
}
