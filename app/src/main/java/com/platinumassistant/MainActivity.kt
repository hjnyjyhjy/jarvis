package com.platinumassistant

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import dagger.hilt.android.AndroidEntryPoint
import com.platinumassistant.ui.theme.PlatinumTheme

/**
 * Main activity for the Platinum Arabic AI Assistant
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            PlatinumTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // TODO: Add main navigation
                    MainContent()
                }
            }
        }
    }
}

/**
 * Main composable content - placeholder for now
 */
@androidx.compose.runtime.Composable
fun MainContent() {
    androidx.compose.material3.Text("Welcome to Platinum Assistant")
}
