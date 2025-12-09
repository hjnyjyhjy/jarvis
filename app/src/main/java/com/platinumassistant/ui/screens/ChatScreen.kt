package com.platinumassistant.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.platinumassistant.domain.entities.Message
import com.platinumassistant.domain.entities.MessageSender

/**
 * Main chat screen for the Platinum Arabic AI Assistant
 * Displays message history and input interface
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(
    messages: List<Message> = emptyList(),
    onSendMessage: (String) -> Unit = {},
    currentPersonality: String = "Jarvis"
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Platinum AI - $currentPersonality") }
            )
        }
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
                items(messages.reversed()) { message ->
                    MessageItem(message = message)
                }
            }

            // Input area
            ChatInputArea(onSendMessage = onSendMessage)
        }
    }
}

/**
 * Individual message display component
 */
@Composable
fun MessageItem(message: Message) {
    val isUserMessage = message.isUserMessage()
    val alignment = if (isUserMessage) Alignment.End else Alignment.Start
    val backgroundColor = if (isUserMessage) 
        MaterialTheme.colorScheme.primary 
    else 
        MaterialTheme.colorScheme.surface

    Box(
        modifier = Modifier
            .fillMaxWidth(0.85f)
            .align(alignment)
            .background(
                backgroundColor,
                shape = MaterialTheme.shapes.medium
            )
            .padding(12.dp)
    ) {
        Text(
            text = message.content,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

/**
 * Chat input component with send button
 */
@Composable
fun ChatInputArea(onSendMessage: (String) -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface)
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Text("Chat input component - To be implemented")
    }
}
