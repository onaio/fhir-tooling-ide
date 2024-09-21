package org.smartregister.fct.presentation.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ListAlt
import androidx.compose.material.icons.outlined.ConnectedTv
import androidx.compose.material.icons.outlined.Insights
import androidx.compose.material.icons.outlined.PhoneAndroid
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstrainedLayoutReference
import androidx.constraintlayout.compose.ConstraintLayoutScope
import org.smartregister.fct.aurora.AuroraIconPack
import org.smartregister.fct.aurora.auroraiconpack.ConnectedTv
import org.smartregister.fct.aurora.auroraiconpack.ListAlt
import org.smartregister.fct.aurora.auroraiconpack.PhoneAndroid
import org.smartregister.fct.aurora.auroraiconpack.SearchInsights
import org.smartregister.fct.common.data.enums.BottomWindowState
import org.smartregister.fct.common.data.enums.RightWindowState
import org.smartregister.fct.common.data.manager.SubWindowManager
import org.smartregister.fct.aurora.presentation.ui.components.SmallIconButton
import org.smartregister.fct.aurora.presentation.ui.components.TooltipPosition

context (ConstraintLayoutScope)
@Composable
fun RightNavigation(
    subWindowManager: SubWindowManager,
    rightNav: ConstrainedLayoutReference
) {

    Row(
        modifier = Modifier.background(MaterialTheme.colorScheme.background)
            .constrainAs(rightNav) {
                top.linkTo(parent.top)
                end.linkTo(parent.end)
                bottom.linkTo(parent.bottom)
            })
    {
        VerticalDivider()
        Column(
            modifier = Modifier.width(45.dp)
                .background(MaterialTheme.colorScheme.surface).fillMaxHeight(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Spacer(Modifier.height(14.dp))
                SmallIconButton(
                    tooltip = "Device Manager",
                    tooltipPosition = TooltipPosition.Left(),
                    delayMillis = 100,
                    icon = AuroraIconPack.PhoneAndroid,
                    onClick = { subWindowManager.setRightWindowState(RightWindowState.DeviceManager) }
                )
                Spacer(Modifier.height(22.dp))
                SmallIconButton(
                    tooltip = "Package Manager",
                    tooltipPosition = TooltipPosition.Left(),
                    delayMillis = 100,
                    icon = AuroraIconPack.ListAlt,
                    onClick = { subWindowManager.setRightWindowState(RightWindowState.PackageManager) }
                )
                Spacer(Modifier.height(22.dp))
                SmallIconButton(
                    tooltip = "Insights",
                    tooltipPosition = TooltipPosition.Left(),
                    delayMillis = 100,
                    icon = AuroraIconPack.SearchInsights,
                    onClick = {  }
                )
            }
            Column {
                SmallIconButton(
                    icon = AuroraIconPack.ConnectedTv,
                    tooltip = "Logcat",
                    tooltipPosition = TooltipPosition.Left(),
                    delayMillis = 100,
                    onClick = { subWindowManager.setBottomWindowState(BottomWindowState.Logcat) }
                )
                Spacer(Modifier.height(12.dp))
            }
        }
    }
}