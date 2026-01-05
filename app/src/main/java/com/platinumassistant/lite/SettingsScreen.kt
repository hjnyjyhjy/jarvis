package com.platinumassistant.lite

import android.content.Context
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog

@Composable
fun SettingsDialog(ctx: Context, show: Boolean, onDismiss: () -> Unit) {
    if (!show) return

    var tts by remember { mutableStateOf(FlagsStore.getFlag(ctx, "tts_enabled", true)) }
    var online by remember { mutableStateOf(FlagsStore.getFlag(ctx, "online_enabled", false)) }
    var textOnly by remember { mutableStateOf(FlagsStore.getFlag(ctx, "text_only", false)) }

    Dialog(onDismissRequest = onDismiss) {
        Surface(shape = MaterialTheme.shapes.medium) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Settings", style = MaterialTheme.typography.titleMedium)
                Divider(modifier = Modifier.padding(vertical = 8.dp))

                RowSetting("Enable TTS", tts) { v -> tts = v; FlagsStore.setFlag(ctx, "tts_enabled", v) }
                RowSetting("Enable Online AI", online) { v -> online = v; FlagsStore.setFlag(ctx, "online_enabled", v) }
                RowSetting("Text-only fallback", textOnly) { v -> textOnly = v; FlagsStore.setFlag(ctx, "text_only", v) }

                Button(onClick = onDismiss, modifier = Modifier.padding(top = 8.dp)) { Text("Close") }

                // Developer credits (both English and Arabic lines)
                val creatorEn = ctx.getString(com.platinumassistant.R.string.creator_name_en)
                val creatorAr = ctx.getString(com.platinumassistant.R.string.creator_name_ar)
                Text(
                    text = creatorEn,
                    style = MaterialTheme.typography.labelSmall,
                    modifier = Modifier.padding(top = 12.dp)
                )
                Text(
                    text = creatorAr,
                    style = MaterialTheme.typography.labelSmall,
                    modifier = Modifier.padding(top = 2.dp)
                )

                // Microphone permission helper
                Divider(modifier = Modifier.padding(vertical = 8.dp))
                Text(ctx.getString(com.platinumassistant.R.string.permission_microphone_message), style = MaterialTheme.typography.bodySmall)
                Button(onClick = {
                    // Open app settings so user can grant microphone permission
                    val uri: Uri = Uri.fromParts("package", ctx.packageName, null)
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, uri)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    ctx.startActivity(intent)
                }, modifier = Modifier.padding(top = 8.dp)) {
                    Text(ctx.getString(com.platinumassistant.R.string.permission_microphone))
                }
            }
        }
    }
}

@Composable
private fun RowSetting(label: String, value: Boolean, onChange: (Boolean) -> Unit) {
    Row(modifier = Modifier.padding(vertical = 6.dp)) {
        Text(label, modifier = Modifier.weight(1f))
        Switch(checked = value, onCheckedChange = onChange)
    }
}
