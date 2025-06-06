package com.example.sofarotator.presentation.theme

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF00BCD4), // Cyan
    secondary = Color(0xFF2196F3), // Blue
    tertiary = Color(0xFF9C27B0), // Purple
    background = Color(0xFF000000), // Black
    surface = Color(0xFF111111),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color.White,
    onSurface = Color.White,
)

@Composable
fun SofaRotatorTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = DarkColorScheme,
        typography = Typography,
        content = content
    )
} 