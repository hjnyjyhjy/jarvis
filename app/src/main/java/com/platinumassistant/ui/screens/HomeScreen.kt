package com.platinumassistant.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.platinumassistant.ui.viewmodels.ListeningViewModel

/**
 * Lightweight modern Home screen optimized for low resource use.
 */
@Composable
fun HomeScreen(
    onNavigateToSettings: () -> Unit = {},
    listeningViewModel: ListeningViewModel
) {
    val isListening = listeningViewModel.isListening.collectAsState()
    val lastTranscript = listeningViewModel.lastTranscript.collectAsState()

    Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
        Column(modifier = Modifier.fillMaxSize()) {
            TopAppBar(title = { Text(text = "${String.format("%s", "Platinum AI")} ") }, actions = {
                IconButton(onClick = onNavigateToSettings) {
                    Icon(imageVector = Icons.Default.Settings, contentDescription = "Settings")
                }
            })

            Column(modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally) {

                Card(modifier = Modifier.padding(8.dp)) {
                    Column(modifier = Modifier.padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(imageVector = Icons.Default.Mic, contentDescription = "Mic", modifier = Modifier.size(64.dp))
                        if (isListening.value) {
                            CircularProgressIndicator(modifier = Modifier.padding(top = 12.dp))
                            Text(text = "يستمع...")
                        } else {
                            Button(onClick = { listeningViewModel.startListening() }, modifier = Modifier.padding(top = 12.dp)) {
                                Text(text = "استمع الآن")
                            }
                        }
                        Text(text = lastTranscript.value, modifier = Modifier.padding(top = 12.dp))
                    }
                }
            }
        }
    }
}
