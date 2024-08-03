package org.smartregister.fct.ui.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import org.smartregister.fct.util.extension.hexToColor

val LightColorScheme = lightColorScheme(
    primary = "#5e5791".hexToColor(),
    secondary = "#AAA6BD".hexToColor(),
    tertiary = "#7b5265".hexToColor(),
    onPrimary = "#ffffff".hexToColor(),
    primaryContainer = "#e4dfff".hexToColor(),
    onPrimaryContainer = "#1a1249".hexToColor(),
    onSecondary = "#1D1B2D".hexToColor(),
    secondaryContainer = "#C8C3DC".hexToColor(),
    onSecondaryContainer = "#1b192c".hexToColor(),
    onTertiary = "#ffffff".hexToColor(),
    onTertiaryContainer = "#ffd8e8".hexToColor(),
    tertiaryContainer = "#ffd8e8".hexToColor(),
    background = "#F9F9F9".hexToColor(),
    onBackground = "#1c1b20".hexToColor(),
    surface = "#fcf8ff".hexToColor(),
    surfaceContainer = "#EBEBEB".hexToColor(),
    onSurface = "#1c1b20".hexToColor(),
    surfaceVariant = "#ddd8e0".hexToColor(),
    onSurfaceVariant = "#47464f".hexToColor(),
    error = "#ba1a1a".hexToColor(),
    onError = "#ffffff".hexToColor(),
    errorContainer = "#ffdad6".hexToColor(),
    onErrorContainer = "#410002".hexToColor(),
    outline = "#78767f".hexToColor(),
)

val DarkColorScheme = darkColorScheme(
    primary = "#c7bfff".hexToColor(),
    secondary = "#474459".hexToColor(),
    tertiary = "#ecb8ce".hexToColor(),
    onPrimary = "#2f295f".hexToColor(),
    primaryContainer = "#463f77".hexToColor(),
    onPrimaryContainer = "#e4dfff".hexToColor(),
    onSecondary = "#F6F1FF".hexToColor(),
    secondaryContainer = "#9A96AD".hexToColor(),
    onSecondaryContainer = "#e5dff9".hexToColor(),
    onTertiary = "#482537".hexToColor(),
    onTertiaryContainer = "#ffd8e8".hexToColor(),
    tertiaryContainer = "#613b4d".hexToColor(),
    background = "#373737".hexToColor(),
    onBackground = "#e5e1e9".hexToColor(),
    surface = "#141318".hexToColor(),
    onSurface = "#e5e1e9".hexToColor(),
    surfaceVariant = "#201f25".hexToColor(),
    onSurfaceVariant = "#c9c5d0".hexToColor(),
    error = "#ffb4ab".hexToColor(),
    onError = "#690005".hexToColor(),
    errorContainer = "#93000a".hexToColor(),
    onErrorContainer = "#ffdad6".hexToColor(),
    outline = "#928f99".hexToColor(),
)

@Composable
fun FCTTheme(
    isDarkModel: Boolean = false,
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (isDarkModel) DarkColorScheme else LightColorScheme,
        typography = UbuntuTypography()
    ) {
        content()
    }
}