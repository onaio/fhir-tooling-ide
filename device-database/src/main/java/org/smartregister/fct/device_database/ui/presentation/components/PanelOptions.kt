package org.smartregister.fct.device_database.ui.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.smartregister.fct.aurora.AuroraIconPack
import org.smartregister.fct.aurora.auroraiconpack.Sync
import org.smartregister.fct.aurora.auroraiconpack.TableEye
import org.smartregister.fct.aurora.presentation.ui.components.SmallIconButton
import org.smartregister.fct.aurora.presentation.ui.components.TooltipPosition

@Composable
internal fun PanelOptions(
    refreshTables: () -> Unit,
    openNewTab: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(39.dp)
            .background(MaterialTheme.colorScheme.surfaceContainer)
            .padding(10.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        SmallIconButton(
            modifier = Modifier.size(18.dp),
            rippleRadius = 14.dp,
            icon = AuroraIconPack.Sync,
            tooltip = "Refresh",
            onClick = refreshTables
        )
        Spacer(Modifier.width(12.dp))
        SmallIconButton(
            modifier = Modifier.size(18.dp),
            rippleRadius = 14.dp,
            tooltip = "New Tab",
            tooltipPosition = TooltipPosition.BOTTOM,
            icon = AuroraIconPack.TableEye,
            onClick = openNewTab
        )
    }
}