package org.smartregister.fct.presentation.ui.components

import androidx.compose.foundation.layout.size
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.ComponentContext
import org.smartregister.fct.aurora.ui.components.Icon
import org.smartregister.fct.aurora.ui.components.SmallIconButton
import org.smartregister.fct.settings.presentation.ui.dialogs.rememberSettingsDialog

@Composable
internal fun SettingButton(
    componentContext: ComponentContext
) {

    val settingsDialog = rememberSettingsDialog(
        componentContext = componentContext
    )

    SmallIconButton(
        onClick = {
            settingsDialog.show()
        },
        icon = Icons.Outlined.Settings,
        alpha = 0.7f
    )
}