package com.platinumassistant

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import dagger.hilt.android.AndroidEntryPoint
import com.platinumassistant.ui.theme.PlatinumTheme
import com.platinumassistant.ui.screens.ChatScreen

/**
 * Main activity for the Platinum Arabic AI Assistant
 * 
 * This is the entry point of the application.
 * Responsibilities:
 * - Initialize Hilt dependency injection
 * - Set up Compose UI theme
 * - Route to main content/navigation
 * - Handle app lifecycle events
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
                    // TODO: Add proper navigation (NavController, NavGraph)
                    MainContent()
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        // TODO: Initialize voice recognition
        // TODO: Load user preferences
        // TODO: Start background services if needed
    }

    override fun onStop() {
        super.onStop()
        // TODO: Save state
        // TODO: Stop voice recognition
    }
}

/**
 * Main composable content - displays chat screen
 * This will be replaced with proper navigation once Router is implemented
 */
@Composable
fun MainContent() {
    ChatScreen(
        messages = emptyList(),
        onSendMessage = { message ->
            // TODO: Handle message sending
        },
        currentPersonality = "Jarvis"
    )
}
