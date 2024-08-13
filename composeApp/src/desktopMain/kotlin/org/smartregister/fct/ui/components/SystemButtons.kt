package org.smartregister.fct.ui.components

import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import fct.composeapp.generated.resources.Res
import fct.composeapp.generated.resources.close
import fct.composeapp.generated.resources.maximize
import fct.composeapp.generated.resources.minimize
import fct.composeapp.generated.resources.restore
import org.jetbrains.compose.resources.painterResource
import org.smartregister.fct.ui.isWindowMaximized

@Composable
fun WindowsActionButtons(
    onRequestClose: () -> Unit,
    onRequestMinimize: (() -> Unit)?,
    onToggleMaximize: (() -> Unit)?,
) {
    Row(
        // Toolbar is aligned center vertically so I fill that and place it on top
        modifier = Modifier.fillMaxHeight().padding(end = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {

        onRequestMinimize?.let {
            IconButton(
                modifier = Modifier.size(30.dp),
                onClick = onRequestMinimize
            ) {
                Icon(
                    modifier = Modifier.scale(0.6f),
                    painter = painterResource(Res.drawable.minimize),
                    contentDescription = null
                )
            }
        }

        Spacer(Modifier.width(4.dp))

        onToggleMaximize?.let {
            IconButton(
                modifier = Modifier.size(30.dp),
                onClick = onToggleMaximize
            ) {
                Icon(
                    modifier = Modifier.scale(0.6f),
                    painter = painterResource(if (isWindowMaximized()) Res.drawable.restore else Res.drawable.maximize),
                    contentDescription = null
                )
            }
        }

        Spacer(Modifier.width(4.dp))

        val interactionSource = remember { MutableInteractionSource() }
        val isHover by interactionSource.collectIsHoveredAsState()

        IconButton(
            modifier = Modifier.size(30.dp).hoverable(interactionSource),
            onClick = onRequestClose,
            colors = IconButtonDefaults.iconButtonColors(
                containerColor = if (isHover) Color.Red else Color.Unspecified
            )
        ) {
            Icon(
                modifier = Modifier.scale(0.5f),
                painter = painterResource(Res.drawable.close),
                tint = if (isHover) Color.White else LocalContentColor.current,
                contentDescription = null,
            )
        }

    }
}