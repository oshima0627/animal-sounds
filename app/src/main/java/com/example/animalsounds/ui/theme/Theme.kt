package com.example.animalsounds.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val AnimalColorScheme = lightColorScheme(
    primary = OrangePrimary,
    onPrimary = androidx.compose.ui.graphics.Color.White,
    primaryContainer = OrangePrimaryLight,
    onPrimaryContainer = TextDark,
    secondary = YellowSecondary,
    onSecondary = TextDark,
    secondaryContainer = YellowSecondaryLight,
    onSecondaryContainer = TextDark,
    background = BackgroundWarm,
    onBackground = TextDark,
    surface = SurfaceWarm,
    onSurface = TextDark,
)

@Composable
fun AnimalSoundsTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = AnimalColorScheme,
        typography = Typography,
        content = content
    )
}
