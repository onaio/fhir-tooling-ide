package org.smartregister.fct.serverconfig.presentation.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Download
import androidx.compose.runtime.Composable
import org.smartregister.fct.aurora.ui.components.TextButton
import org.smartregister.fct.serverconfig.presentation.components.ServerConfigPanelComponent

@Composable
internal fun ImportConfigButton(component: ServerConfigPanelComponent) {
    TextButton(
        label = "Import",
        icon = Icons.Outlined.Download,
        onClick = {}
    )
}