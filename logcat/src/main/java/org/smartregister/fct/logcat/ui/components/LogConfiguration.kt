package org.smartregister.fct.logcat.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.WrapText
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Pause
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.smartregister.fct.logcat.FCTLogger

@Composable
internal fun LogConfiguration() {

    val isPause by FCTLogger.getPause().collectAsState(initial = false)

    Column(
        modifier = Modifier.fillMaxHeight().background(MaterialTheme.colorScheme.surfaceContainer).width(40.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(6.dp))
        SmallIconButton(
            icon = Icons.Outlined.Delete,
            onClick = { FCTLogger.clearLogs() }
        )
        Spacer(Modifier.height(3.dp))
        SmallIconButton(
            icon = if(isPause) Icons.Rounded.PlayArrow else Icons.Outlined.Pause,
            onClick = { FCTLogger.togglePause() }
        )
        Spacer(Modifier.height(3.dp))
        SmallIconButton(
            icon = Icons.AutoMirrored.Outlined.WrapText,
            onClick = {}
        )
    }
}