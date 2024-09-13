package org.smartregister.fct.aurora.presentation.ui.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SmallIconButton(
    modifier: Modifier = Modifier.size(18.dp),
    icon: ImageVector,
    enable: Boolean = true,
    tint: Color? = null,
    alpha: Float = 1f,
    tooltip: String? = null,
    tooltipPosition: TooltipPosition = TooltipPosition.Bottom(),
    rippleRadius: Dp = 14.dp,
    onClick: () -> Unit
) {

    var mainModifier = modifier.width(22.dp)

    if (enable) {
        mainModifier = mainModifier.clickable(
            onClick = onClick,
            interactionSource = remember { MutableInteractionSource() },
            indication = rememberRipple(bounded = false, radius = rippleRadius)
        )
    }

    val composable: @Composable () -> Unit = {
        Box(
            modifier = mainModifier,
            contentAlignment = Alignment.Center
        ) {
            Icon(
                modifier = modifier.align(Alignment.Center).width(22.dp),
                imageVector = icon,
                contentDescription = null,
                tint = if(enable) tint?.copy(alpha = alpha) ?: LocalContentColor.current.copy(alpha = alpha) else LocalContentColor.current.copy(alpha = 0.3f)
            )
        }
    }

    if (tooltip == null) {
        composable()
    } else {
        Tooltip(
            tooltip = tooltip,
            tooltipPosition = tooltipPosition
        ) {
            composable()
        }
    }



}
