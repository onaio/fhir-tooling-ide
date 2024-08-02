package org.smartregister.fct.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ConnectedTv
import androidx.compose.material.icons.rounded.PhoneAndroid
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import org.smartregister.fct.engine.data.locals.LocalRightNavigationViewModel

@Composable
fun RightNavigation() {
    val viewModel = LocalRightNavigationViewModel.current
    VerticalDivider()
    Column(
        modifier = Modifier.width(45.dp)
            .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.5f)).fillMaxHeight(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            HeightSpacer(12.dp)
            MenuButton(
                icon = Icons.Rounded.PhoneAndroid,
                onClick = {}
            )
        }
        Column {
            MenuButton(
                icon = Icons.Outlined.ConnectedTv,
                onClick = { viewModel.toggleLogWindow() }
            )
            HeightSpacer(12.dp)
        }
    }
}

@Composable
private fun MenuButton(
    icon: ImageVector,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
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