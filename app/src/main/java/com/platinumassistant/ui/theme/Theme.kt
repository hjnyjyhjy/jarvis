package com.platinumassistant.ui.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF1F88E5),
    onPrimary = Color.White,
    primaryContainer = Color(0xFF0D47A1),
    onPrimaryContainer = Color(0xFF81D4FA),
    secondary = Color(0xFFFF6B6B),
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFFF1744),
    onSecondaryContainer = Color(0xFFFFAB91),
    tertiary = Color(0xFF10B981),
    onTertiary = Color.White,
    background = Color(0xFF121212),
    onBackground = Color.White,
    surface = Color(0xFF1E1E1E),
    onSurface = Color.White,
    error = Color(0xFFDC2626),
    onError = Color.White,
)

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF1F88E5),
    onPrimary = Color.White,
    primaryContainer = Color(0xFFE3F2FD),
    onPrimaryContainer = Color(0xFF0D47A1),
    secondary = Color(0xFFFF6B6B),
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFFFEBEE),
    onSecondaryContainer = Color(0xFFB71C1C),
    tertiary = Color(0xFF10B981),
    onTertiary = Color.White,
    background = Color.White,
    onBackground = Color(0xFF212121),
    surface = Color(0xFFF5F5F5),
    onSurface = Color(0xFF212121),
    error = Color(0xFFDC2626),
    onError = Color.White,
)

@Composable
fun PlatinumTheme(
    darkTheme: Boolean = androidx.compose.material3.isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
