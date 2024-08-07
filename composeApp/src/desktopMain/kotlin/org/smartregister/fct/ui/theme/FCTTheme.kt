package org.smartregister.fct.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import org.smartregister.fct.util.extension.hexToColor

val LightColorScheme = lightColorScheme(
    primary = "#74739A".hexToColor(),
    secondary = "#9C98B1".hexToColor(),
    tertiary = "#EC6C34".hexToColor(),
    onPrimary = "#F2EFFF".hexToColor(),
    primaryContainer = "#AEADD7".hexToColor(),
    onPrimaryContainer = "#1a1249".hexToColor(),
    onSecondary = "#262337".hexToColor(),
    secondaryContainer = "#C5C1DA".hexToColor(),
    onSecondaryContainer = "#221F32".hexToColor(),
    onTertiary = "#FFFFFF".hexToColor(),
    onTertiaryContainer = "#FFCBC5".hexToColor(),
    tertiaryContainer = "#EB292B".hexToColor(),
    background = "#F9F9F9".hexToColor(),
    onBackground = "#1c1b20".hexToColor(),
    surface = "#F9F9F9".hexToColor(),
    surfaceContainer = "#EBEBEB".hexToColor(),
    onSurface = "#1c1b20".hexToColor(),
    surfaceVariant = "#DADADA".hexToColor(),
    onSurfaceVariant = "#47464f".hexToColor(),
    error = "#ba1a1a".hexToColor(),
    onError = "#ffffff".hexToColor(),
    errorContainer = "#ffdad6".hexToColor(),
    onErrorContainer = "#410002".hexToColor(),
    outline = "#78767f".hexToColor(),
)

val DarkColorScheme = darkColorScheme(
    primary = "#74739A".hexToColor(),
    secondary = "#474459".hexToColor(),
    tertiary = "#EC6C34".hexToColor(),
    onPrimary = "#F2EFFF".hexToColor(),
    primaryContainer = "#AEADD7".hexToColor(),
    onPrimaryContainer = "#e4dfff".hexToColor(),
    onSecondary = "#BBB6CF".hexToColor(),
    secondaryContainer = "#8F8BA2".hexToColor(),
    onSecondaryContainer = "#e5dff9".hexToColor(),
    onTertiary = "#FFFFFF".hexToColor(),
    onTertiaryContainer = "#FFCBC5".hexToColor(),
    tertiaryContainer = "#EB292B".hexToColor(),
    background = "#3B3B3B".hexToColor(),
    onBackground = "#e5e1e9".hexToColor(),
    surface = "#2C2C2C".hexToColor(),
    onSurface = "#e5e1e9".hexToColor(),
    surfaceContainer = "#212222".hexToColor(),
    surfaceVariant = "#323232".hexToColor(),
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