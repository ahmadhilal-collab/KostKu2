package com.kostku.app.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp

private val KostKuColors = lightColorScheme(
    primary = Green700,
    onPrimary = White,
    primaryContainer = Green100,
    onPrimaryContainer = Green900,
    secondary = Gold500,
    onSecondary = Ink900,
    secondaryContainer = Gold100,
    onSecondaryContainer = Ink900,
    background = White,
    onBackground = Ink900,
    surface = White,
    onSurface = Ink900,
    surfaceVariant = Green50,
    onSurfaceVariant = Ink600,
    outline = Line,
    outlineVariant = Line,
    error = Danger,
    onError = White,
    errorContainer = DangerBg,
    onErrorContainer = Danger
)

private val KostKuShapes = Shapes(
    extraSmall = RoundedCornerShape(8.dp),
    small = RoundedCornerShape(10.dp),
    medium = RoundedCornerShape(14.dp),
    large = RoundedCornerShape(20.dp),
    extraLarge = RoundedCornerShape(28.dp)
)

@Composable
fun KostKuTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = KostKuColors,
        typography = KostKuTypography,
        shapes = KostKuShapes,
        content = content
    )
}
