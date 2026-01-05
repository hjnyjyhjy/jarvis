package com.platinumassistant.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.IconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalContext
import androidx.work.WorkInfo
import androidx.work.WorkManager

import com.platinumassistant.R
import com.platinumassistant.core.workers.ModelDownloadManager
import com.platinumassistant.core.models.ModelRepository
import com.platinumassistant.core.models.ModelEntry
import com.platinumassistant.core.models.ModelUtils
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.ui.unit.sp
import androidx.compose.material3.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material3.Text as MText
import androidx.compose.material3.Switch
import androidx.compose.runtime.collectAsState
import com.platinumassistant.core.prefs.PreferencesManager
import android.content.Intent
import com.platinumassistant.core.wake.WakeWordService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.material3.TextButton
import androidx.compose.material3.Divider
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch

@Composable
fun ModelRow(model: ModelEntry, context: android.content.Context, installed: Boolean = false) {
    val workName = "download_${model.filename}"
    val workInfos by WorkManager.getInstance(context)
        .getWorkInfosForUniqueWorkLiveData(workName)
        .observeAsState()

    Card(modifier = Modifier
        .fillMaxWidth()
        .padding(vertical = 6.dp)
    ) {
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                MText(text = model.id, fontSize = 16.sp)
                MText(text = "${model.size_bytes ?: 0L / 1024} bytes", fontSize = 12.sp)
            }

            // Determine state
            if (!workInfos.isNullOrEmpty()) {
                val info = workInfos!![0]
                val prog = info.progress.getInt("progress", 0)
                Column(horizontalAlignment = Alignment.End) {
                    LinearProgressIndicator(progress = prog / 100f, modifier = Modifier.padding(bottom = 4.dp))
                    MText(text = "${prog}%")
                    Row {
                        androidx.compose.material3.IconButton(onClick = {
                            ModelDownloadManager.cancelDownload(context, model.filename)
                            Toast.makeText(context, "إلغاء التنزيل...", Toast.LENGTH_SHORT).show()
                        }) {
                            Icon(imageVector = Icons.Default.Cancel, contentDescription = "Cancel")
                        }
                    }
                }
            } else {
                // not running
                Row {
                    androidx.compose.material3.IconButton(onClick = {
                        ModelDownloadManager.enqueueDownload(context, model.url, model.filename)
                        Toast.makeText(context, "بدء تنزيل ${model.id}...", Toast.LENGTH_SHORT).show()
                    }) {
                        Icon(imageVector = Icons.Default.Download, contentDescription = "Download")
                    }

                    if (installed) {
                        androidx.compose.material3.IconButton(onClick = {
                            val deleted = ModelUtils.deleteModel(context, model)
                            Toast.makeText(context, if (deleted) "تم حذف النموذج" else "فشل حذف النموذج", Toast.LENGTH_SHORT).show()
                        }) {
                            Icon(imageVector = Icons.Default.Cancel, contentDescription = "Delete")
                        }
                    }
                }
            }
        }
    }
}
/**
 * Simple settings screen: shows creator and offline instructions.
 * The actual download logic for models should be implemented in a ViewModel
 * or a background worker; here we provide the UI and hooks.
 */
@Composable
fun SettingsScreen(
    onBack: () -> Unit = {},
    onDownloadModels: () -> Unit = {}
) {
    val context = LocalContext.current
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(id = R.string.settings)) }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = stringResource(id = R.string.creator_label) + ":",
                style = MaterialTheme.typography.labelLarge
            )

            Text(
                text = stringResource(id = R.string.creator_name),
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(top = 4.dp, bottom = 12.dp)
            )

            Text(
                text = stringResource(id = R.string.offline_mode_title) + ":",
                style = MaterialTheme.typography.labelLarge
            )

            Text(
                text = stringResource(id = R.string.offline_mode_message),
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(top = 4.dp, bottom = 12.dp)
            )

            // Load manifest and show list of models
            val modelsState = remember { mutableStateListOf<ModelEntry>() }

            LaunchedEffect(Unit) {
                val list = ModelRepository.loadManifest(context)
                modelsState.clear()
                modelsState.addAll(list)
            }

            // Wake-word toggle (low-power listening)
            val wakeFlow = PreferencesManager.isWakeWordEnabled(context)
            val wakeEnabled by wakeFlow.collectAsState(initial = false)

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                MText(text = "تفعيل كلمة الاستيقاظ")
                Switch(checked = wakeEnabled, onCheckedChange = { enabled ->
                    CoroutineScope(Dispatchers.IO).launch {
                        PreferencesManager.setWakeWordEnabled(context, enabled)
                    }
                    if (enabled) {
                        // start foreground wake service
                        val i = Intent(context, WakeWordService::class.java)
                        context.startForegroundService(i)
                    } else {
                        val i = Intent(context, WakeWordService::class.java)
                        context.stopService(i)
                    }
                })
            }

            // Language selection
            val langFlow = PreferencesManager.getLanguage(context)
            val lang by langFlow.collectAsState(initial = "auto")
            MText(text = "اللغة: ${lang}")
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Start) {
                val scope = rememberCoroutineScope()
                TextButton(onClick = { scope.launch(Dispatchers.IO) { PreferencesManager.setLanguage(context, "auto") } }) { Text(text = "Auto") }
                TextButton(onClick = { scope.launch(Dispatchers.IO) { PreferencesManager.setLanguage(context, "ar") } }) { Text(text = "Arabic") }
                TextButton(onClick = { scope.launch(Dispatchers.IO) { PreferencesManager.setLanguage(context, "en") } }) { Text(text = "English") }
            }

            // Online / Offline mode
            val onlineFlow = PreferencesManager.isOnlineModeEnabled(context)
            val onlineEnabled by onlineFlow.collectAsState(initial = false)

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                MText(text = "وضع الإنترنت (Online)")
                Switch(checked = onlineEnabled, onCheckedChange = { enabled ->
                    CoroutineScope(Dispatchers.IO).launch {
                        PreferencesManager.setOnlineModeEnabled(context, enabled)
                    }
                })
            }

            // NLU / ASR endpoint fields
            var nluUrl by remember { mutableStateOf("") }
            var asrUrl by remember { mutableStateOf("") }

            // initialize fields from prefs
            LaunchedEffect(Unit) {
                PreferencesManager.getOnlineNluUrl(context).collect { url -> nluUrl = url ?: "" }
            }
            LaunchedEffect(Unit) {
                PreferencesManager.getOnlineAsrUrl(context).collect { url -> asrUrl = url ?: "" }
            }

            OutlinedTextField(value = nluUrl, onValueChange = { nluUrl = it }, modifier = Modifier.fillMaxWidth(), label = { Text("NLU endpoint (POST)") })
            Button(onClick = {
                CoroutineScope(Dispatchers.IO).launch { PreferencesManager.setOnlineNluUrl(context, if (nluUrl.isBlank()) null else nluUrl) }
                Toast.makeText(context, "تم حفظ إعدادات الـ NLU", Toast.LENGTH_SHORT).show()
            }) {
                Text(text = "حفظ NLU endpoint")
            }

            OutlinedTextField(value = asrUrl, onValueChange = { asrUrl = it }, modifier = Modifier.fillMaxWidth(), label = { Text("ASR endpoint (optional)") })
            Button(onClick = {
                CoroutineScope(Dispatchers.IO).launch { PreferencesManager.setOnlineAsrUrl(context, if (asrUrl.isBlank()) null else asrUrl) }
                Toast.makeText(context, "تم حفظ إعدادات الـ ASR (إن وُجد)", Toast.LENGTH_SHORT).show()
            }) {
                Text(text = "حفظ ASR endpoint")
            }

            Divider(modifier = Modifier.padding(vertical = 12.dp))

            // TTS Engine selection
            val ttsFlow = PreferencesManager.getTtsEngine(context)
            val ttsEngine by ttsFlow.collectAsState(initial = "android")
            MText(text = "محرك تحويل النص لصوت (TTS): ${ttsEngine}")
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Start) {
                TextButton(onClick = {
                    val scope = rememberCoroutineScope()
                    scope.launch(Dispatchers.IO) { PreferencesManager.setTtsEngine(context, "android") }
                    Toast.makeText(context, "استخدمت محرك Android TTS", Toast.LENGTH_SHORT).show()
                }) { Text(text = "Android TTS") }
                TextButton(onClick = {
                    val scope = rememberCoroutineScope()
                    scope.launch(Dispatchers.IO) { PreferencesManager.setTtsEngine(context, "coqui") }
                    Toast.makeText(context, "تم اختيار Coqui (خطة مستقبلية)", Toast.LENGTH_SHORT).show()
                }) { Text(text = "Coqui (Offline - planned)") }
            }

            Divider(modifier = Modifier.padding(vertical = 12.dp))

            // Power mode selection
            val powerFlow = PreferencesManager.getPowerMode(context)
            val powerMode by powerFlow.collectAsState(initial = "balanced")
            MText(text = "وضع الطاقة: ${powerMode}")
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Start) {
                val scope = rememberCoroutineScope()
                TextButton(onClick = { scope.launch(Dispatchers.IO) { PreferencesManager.setPowerMode(context, "performance") } }) { Text(text = "Performance") }
                TextButton(onClick = { scope.launch(Dispatchers.IO) { PreferencesManager.setPowerMode(context, "balanced") } }) { Text(text = "Balanced") }
                TextButton(onClick = { scope.launch(Dispatchers.IO) { PreferencesManager.setPowerMode(context, "battery_saver") } }) { Text(text = "Battery saver") }
            }

            if (modelsState.isEmpty()) {
                Text(text = "لا توجد نماذج لمعرفة المزيد.")
            } else {
                LazyColumn(modifier = Modifier.fillMaxWidth()) {
                    items(modelsState) { model ->
                        val installed = ModelUtils.getInstalledModelPath(context, model) != null
                        ModelRow(model = model, context = context, installed = installed)
                    }
                }
            }
        }
    }
}
