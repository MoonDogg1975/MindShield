package com.mindshield.app.ui.preview

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.mindshield.app.ui.theme.MindShieldTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PreviewActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Get the preview name from the intent
        val previewName = intent.getStringExtra(PREVIEW_NAME) ?: "LoginScreen"
        
        setContent {
            MindShieldTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    when (previewName) {
                        "LoginScreen" -> LoginScreenPreview()
                        "LoginScreenDark" -> LoginScreenDarkPreview()
                        "RegisterScreen" -> RegisterScreenPreview()
                        "RegisterScreenDark" -> RegisterScreenDarkPreview()
                        else -> LoginScreenPreview()
                    }
                }
            }
        }
    }
    
    companion object {
        const val PREVIEW_NAME = "preview_name"
    }
}
