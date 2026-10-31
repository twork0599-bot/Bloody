package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LifedropDarkColorScheme = darkColorScheme(
    primary = CrimsonLight,
    onPrimary = Color.White,
    primaryContainer = CrimsonContainerDark,
    onPrimaryContainer = Color(0xFFFFDADA),
    secondary = CrimsonPrimary,
    onSecondary = Color.White,
    tertiary = MedicalGreen,
    onTertiary = Color.White,
    background = CharcoalBackground,
    onBackground = TextPrimaryDark,
    surface = CharcoalSurface,
    onSurface = TextPrimaryDark,
    surfaceVariant = CharcoalSurfaceVariant,
    onSurfaceVariant = TextSecondaryDark,
    outline = CharcoalBorder,
    error = EmergencyRed,
    onError = Color.White
)

private val LifedropLightColorScheme = lightColorScheme(
    primary = CrimsonPrimary,
    onPrimary = Color.White,
    primaryContainer = CrimsonContainer,
    onPrimaryContainer = CrimsonDark,
    secondary = CrimsonDark,
    onSecondary = Color.White,
    tertiary = MedicalGreen,
    onTertiary = Color.White,
    background = LightBackground,
    onBackground = TextPrimaryLight,
    surface = LightSurface,
    onSurface = TextPrimaryLight,
    surfaceVariant = LightSurfaceVariant,
    onSurfaceVariant = TextSecondaryLight,
    outline = LightBorder,
    error = EmergencyRed,
    onError = Color.White
)

@Composable
fun LifedropTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) LifedropDarkColorScheme else LifedropLightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
