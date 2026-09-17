package com.moneytracker.android.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

private val LightColors = lightColorScheme(
    primary = Color(0xFF075E48), onPrimary = Color.White,
    primaryContainer = Color(0xFFD5F9E6), onPrimaryContainer = Color(0xFF00382A),
    secondary = Color(0xFF4C635A), background = Color(0xFFF5F8F7),
    surface = Color(0xFFF5F8F7), surfaceContainer = Color(0xFFE9EFEB),
    onSurface = Color(0xFF17251F), onSurfaceVariant = Color(0xFF44534B),
)
private val DarkColors = darkColorScheme(
    primary = Color(0xFF92D8B8), onPrimary = Color(0xFF003829),
    primaryContainer = Color(0xFF07533F), onPrimaryContainer = Color(0xFFD5F9E6),
    secondary = Color(0xFFB3CCBF), background = Color(0xFF101815),
    surface = Color(0xFF101815), surfaceContainer = Color(0xFF1C2922),
    onSurface = Color(0xFFE0EBE4), onSurfaceVariant = Color(0xFFB9C9BE),
)

@Composable
fun MoneyTrackerTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColors else LightColors,
        typography = AppTypography,
        shapes = Shapes(medium = RoundedCornerShape(20.dp), large = RoundedCornerShape(28.dp)),
        content = content,
    )
}
