package com.example.animalsounds.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val AnimalLightColorScheme = lightColorScheme(
    primary = OrangePrimary,
    onPrimary = Color.White,
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

private val AnimalDarkColorScheme = darkColorScheme(
    primary = DarkPrimary,
    onPrimary = DarkOnPrimary,
    primaryContainer = OrangePrimaryDark,
    onPrimaryContainer = DarkOnBackground,
    secondary = YellowSecondary,
    onSecondary = TextDark,
    secondaryContainer = OrangePrimaryDark,
    onSecondaryContainer = DarkOnBackground,
    background = DarkBackground,
    onBackground = DarkOnBackground,
    surface = DarkSurface,
    onSurface = DarkOnSurface,
)

@Composable
fun AnimalSoundsTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) AnimalDarkColorScheme else AnimalLightColorScheme
    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
