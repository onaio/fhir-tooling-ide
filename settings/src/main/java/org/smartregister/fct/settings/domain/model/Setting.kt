package org.smartregister.fct.settings.domain.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Dns
import androidx.compose.material.icons.outlined.Subtitles
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Setting(val label: String, val icon: ImageVector) {
    data object ServerConfigs : Setting("Server Configs", Icons.Outlined.Dns)
    data object CodeEditor : Setting("Code Editor", Icons.Outlined.Subtitles)
}