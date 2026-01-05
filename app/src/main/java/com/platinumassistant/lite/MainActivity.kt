package com.platinumassistant.lite

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

// Compose-based MainActivity follows. Keeping a single MainActivity (Compose) implementation
package com.platinumassistant.lite

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import android.content.pm.PackageManager
import android.content.Context
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

// Small, focused MainActivity for Jarvis Lite
class MainActivity : ComponentActivity() {
    private val vm: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    MainScreen(vm)
                }
            }
        }
    }
}

@Composable
fun MainScreen(vm: MainViewModel) {
    val state by vm.uiState.collectAsState()
    val ctx = LocalContext.current
    var showSettings by remember { mutableStateOf(false) }
    var showPermissionDialog by remember { mutableStateOf(false) }

    // Permission launcher for RECORD_AUDIO
    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) {
            vm.onMicPressed(ctx)
        } else {
            showPermissionDialog = true
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // Top bar with settings
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Text("Jarvis Lite", style = MaterialTheme.typography.titleLarge, modifier = Modifier.weight(1f))
            IconButton(onClick = { showSettings = true }) {
                Icon(Icons.Default.Settings, contentDescription = "Settings")
            }
        }

        if (showSettings) SettingsDialog(ctx, show = showSettings) { showSettings = false }
        // Response area
        Text(text = state.responseText, modifier = Modifier.weight(1f))

        // Mic and controls
        val tts = remember { TtsWrapper(ctx) }
        DisposableEffect(Unit) {
            onDispose { tts.shutdown() }
        }

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
            Button(onClick = {
                // Check runtime permission first
                val granted = ContextCompat.checkSelfPermission(ctx, android.Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED
                if (granted) vm.onMicPressed(ctx)
                else permissionLauncher.launch(android.Manifest.permission.RECORD_AUDIO)
            }) {
                Text(if (state.isListening) "Stop" else "Speak")
            }
            Spacer(modifier = Modifier.width(8.dp))
            Button(onClick = {
                if (FlagsStore.getFlag(ctx, "tts_enabled", true)) tts.speak(state.responseText)
                else vm.setResponse("TTS disabled in settings")
            }) {
                Text("Speak")
            }
        }
        if (showPermissionDialog) {
            AlertDialog(
                onDismissRequest = { showPermissionDialog = false },
                title = { Text(text = ctx.getString(com.platinumassistant.R.string.permission_microphone)) },
                text = { Text(text = ctx.getString(com.platinumassistant.R.string.permission_microphone_message)) },
                confirmButton = {
                    TextButton(onClick = {
                        // Open app settings
                        val uri = android.net.Uri.fromParts("package", ctx.packageName, null)
                        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, uri).apply { addFlags(Intent.FLAG_ACTIVITY_NEW_TASK) }
                        ctx.startActivity(intent)
                        showPermissionDialog = false
                    }) { Text(ctx.getString(com.platinumassistant.R.string.settings)) }
                },
                dismissButton = {
                    TextButton(onClick = { showPermissionDialog = false }) { Text(ctx.getString(com.platinumassistant.R.string.cancel)) }
                }
            )
        }
    }
}

// Simple UI state container
data class UiState(val responseText: String = "Ready", val isListening: Boolean = false)

import android.content.Context

class MainViewModel : ViewModel() {
    private val _uiState = mutableStateOf(UiState())
    val uiState: State<UiState> get() = _uiState

    // Lazy init VoiceInput to save resources
    private var voiceInput: VoiceInput? = null

    fun onMicPressed(context: Context) {
        if (voiceInput == null) voiceInput = VoiceInput(context)

        if (_uiState.value.isListening) {
            voiceInput?.stopListening()
            _uiState.value = _uiState.value.copy(isListening = false)
        } else {
            _uiState.value = _uiState.value.copy(isListening = true, responseText = "Listening...")
            // Use short coroutine to trigger listening (actual impl in VoiceInput)
            viewModelScope.launch {
                voiceInput?.startListening { text ->
                    // Parse simple commands and execute small actions
                    val res = CommandParser.parseAndExecute(context, text)
                    _uiState.value = UiState(responseText = res, isListening = false)
                }
            }
        }
    }

    // allow UI to set response text directly
    fun setResponse(text: String) {
        _uiState.value = UiState(responseText = text, isListening = false)
    }
}
