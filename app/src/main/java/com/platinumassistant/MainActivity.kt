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
import com.platinumassistant.ui.screens.SettingsScreen
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import android.content.IntentFilter
import androidx.activity.viewModels
import com.platinumassistant.ui.viewmodels.ListeningViewModel
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Bundle

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

    private val listeningViewModel: ListeningViewModel by viewModels()

    private val wakeReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent?.action == "com.platinumassistant.ACTION_WAKE_DETECTED") {
                // Start STT and update UI via ViewModel
                listeningViewModel.startListening()
            }
        }
    }

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
                        MainContent(listeningViewModel = listeningViewModel)
                }
            }
        }
            // Register receiver
            registerReceiver(wakeReceiver, IntentFilter("com.platinumassistant.ACTION_WAKE_DETECTED"))
            // Init Vosk manager
            listeningViewModel.init(this)
            // Init TTS
            com.platinumassistant.core.tts.SynthesizerManager.initialize(this)
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

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(wakeReceiver)
        com.platinumassistant.core.tts.SynthesizerManager.shutdown()
    }
}

/**
 * Main composable content - displays chat screen
 * This will be replaced with proper navigation once Router is implemented
 */
@Composable
fun MainContent(listeningViewModel: ListeningViewModel) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "home") {
        composable("home") {
            com.platinumassistant.ui.screens.HomeScreen(onNavigateToSettings = { navController.navigate("settings") }, listeningViewModel = listeningViewModel)
        }
        composable("chat") {
            ChatScreen(onNavigateToSettings = { navController.navigate("settings") })
        }
        composable("settings") {
            SettingsScreen(onBack = { navController.popBackStack() })
        }
    }
}
