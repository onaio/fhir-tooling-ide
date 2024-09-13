package org.smartregister.fct.presentation.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.runtime.Composable
import com.arkivanov.decompose.ComponentContext
import org.smartregister.fct.aurora.presentation.ui.components.SmallIconButton
import org.smartregister.fct.aurora.presentation.ui.components.TooltipPosition
import org.smartregister.fct.settings.presentation.ui.dialogs.rememberSettingsDialog

@Composable
internal fun SettingButton(
    componentContext: ComponentContext
) {

    val settingsDialog = rememberSettingsDialog(
        componentContext = componentContext
    )

    SmallIconButton(
        tooltip = "Settings",
        tooltipPosition = TooltipPosition.Bottom(),
        onClick = {
            settingsDialog.show()
        },
        icon = Icons.Outlined.Settings,
        alpha = 0.7f
    )
}