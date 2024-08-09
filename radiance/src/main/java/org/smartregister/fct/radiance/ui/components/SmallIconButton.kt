package org.smartregister.fct.radiance.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.width
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

@Composable
fun SmallIconButton(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            //.minimumInteractiveComponentSize()
            .width(22.dp)
            .clickable(
                onClick = onClick,
                interactionSource = remember { MutableInteractionSource() },
                indication = rememberRipple(bounded = false, radius = 18.dp)
            ),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            modifier = Modifier.width(22.dp),
            imageVector = icon,
            contentDescription = null
        )
    }
}
