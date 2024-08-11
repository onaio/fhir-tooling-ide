package org.smartregister.fct.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import org.smartregister.fct.engine.data.enums.BottomWindowState
import org.smartregister.fct.engine.data.enums.RightWindowState
import org.smartregister.fct.engine.data.viewmodel.SubWindowViewModel
import org.smartregister.fct.radiance.ui.components.SmallIconButton

@Composable
fun RightNavigation(subWindowViewModel: SubWindowViewModel) {

    VerticalDivider()
    Column(
        modifier = Modifier.width(45.dp)
            .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.5f)).fillMaxHeight(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Spacer(Modifier.height(12.dp))
            SmallIconButton(
                icon = Icons.Outlined.PhoneAndroid,
                onClick = { subWindowViewModel.setRightWindowState(RightWindowState.DeviceManager) }
            )
            Spacer(Modifier.height(18.dp))
            SmallIconButton(
                icon = Icons.AutoMirrored.Outlined.ListAlt,
                onClick = { subWindowViewModel.setRightWindowState(RightWindowState.PackageManager) }
            )
            Spacer(Modifier.height(18.dp))
            SmallIconButton(
                icon = Icons.Outlined.Insights,
                onClick = {  }
            )
        }
        Column {
            SmallIconButton(
                icon = Icons.Outlined.ConnectedTv,
                onClick = { subWindowViewModel.setBottomWindowState(BottomWindowState.Logcat) }
            )
            Spacer(Modifier.height(12.dp))
        }
    }
}