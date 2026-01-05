package com.platinumassistant.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.platinumassistant.domain.entities.Message
import com.platinumassistant.ui.viewmodels.ChatViewModel

/**
 * Main chat screen for the Platinum Arabic AI Assistant
 * Displays message history and input interface
 * Connected to ChatViewModel for state management
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(
    onNavigateToSettings: () -> Unit = {},
    viewModel: ChatViewModel = hiltViewModel()
) {
    val chatState = viewModel.chatState.collectAsState()
    val messageInput = viewModel.messageInput.collectAsState()
    val isLoading = viewModel.isLoading.collectAsState()
    val errorMessage = viewModel.errorMessage.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    
    // Show error messages
    LaunchedEffect(errorMessage.value) {
        errorMessage.value?.let {
            snackbarHostState.showSnackbar(it)
        }
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("${chatState.value.selectedPersonality?.name ?: "Platinum AI"}")
                },
                actions = {
                    IconButton(onClick = onNavigateToSettings) {
                        Icon(
                            imageVector = androidx.compose.material.icons.Icons.Default.Settings,
                            contentDescription = "${androidx.compose.ui.res.stringResource(id = com.platinumassistant.R.string.settings)}"
                        )
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Messages list
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                reverseLayout = true
            ) {
                items(chatState.value.messages.reversed()) { message ->
                    MessageItem(
                        message = message,
                        onDelete = { viewModel.deleteMessage(message.id) }
                    )
                }
            }

            // Input area
            ChatInputArea(
                input = messageInput.value,
                onInputChange = { viewModel.updateMessageInput(it) },
                onSendMessage = { viewModel.sendMessage(it) },
                isLoading = isLoading.value,
                onClearHistory = { viewModel.clearChatHistory() }
            )
        }
    }
}


/**
 * Individual message display component
 */
@Composable
fun MessageItem(
    message: Message,
    onDelete: () -> Unit = {}
) {
    val isUserMessage = message.isUserMessage()
    val alignment = if (isUserMessage) Alignment.End else Alignment.Start
    val backgroundColor = if (isUserMessage) 
        MaterialTheme.colorScheme.primary 
    else 
        MaterialTheme.colorScheme.surfaceVariant
    val textColor = if (isUserMessage)
        MaterialTheme.colorScheme.onPrimary
    else
        MaterialTheme.colorScheme.onSurface

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp, horizontal = 8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(0.85f)
                .align(alignment)
                .background(
                    backgroundColor,
                    shape = MaterialTheme.shapes.medium
                )
                .padding(12.dp)
        ) {
            // Sender name
            Text(
                text = message.sender.name,
                style = MaterialTheme.typography.labelSmall,
                color = textColor.copy(alpha = 0.7f),
                modifier = Modifier.padding(bottom = 4.dp)
            )
            
            // Message content
            Text(
                text = message.content,
                style = MaterialTheme.typography.bodyMedium,
                color = textColor
            )
            
            // Timestamp
            Text(
                text = formatTimestamp(message.timestamp),
                style = MaterialTheme.typography.labelSmall,
                color = textColor.copy(alpha = 0.6f),
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}

/**
 * Format timestamp to readable string
 */
fun formatTimestamp(timestamp: Long): String {
    val sdf = java.text.SimpleDateFormat("HH:mm", java.util.Locale.getDefault())
    return sdf.format(java.util.Date(timestamp))
}

/**
 * Chat input component with send button
 */
@Composable
fun ChatInputArea(
    input: String = "",
    onInputChange: (String) -> Unit = {},
    onSendMessage: (String) -> Unit = {},
    isLoading: Boolean = false,
    onClearHistory: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface)
    ) {
        // Control buttons row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(
                onClick = onClearHistory,
                modifier = Modifier.padding(end = 4.dp),
                enabled = !isLoading
            ) {
                Text("Clear")
            }
        }
        
        // Input field with send button
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            TextField(
                value = input,
                onValueChange = onInputChange,
                placeholder = { Text("Type a message...") },
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 8.dp),
                enabled = !isLoading,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send),
                keyboardActions = KeyboardActions(
                    onSend = {
                        if (input.isNotBlank()) {
                            onSendMessage(input)
                        }
                    }
                ),
                singleLine = true
            )
            
            // Send button
            IconButton(
                onClick = {
                    if (input.isNotBlank()) {
                        onSendMessage(input)
                    }
                },
                enabled = !isLoading && input.isNotBlank()
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        strokeWidth = 2.dp
                    )
                } else {
                    Icon(
                        imageVector = Icons.Filled.Send,
                        contentDescription = "Send message"
                    )
                }
            }
        }
    }
}
