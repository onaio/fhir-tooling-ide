package org.smartregister.fct.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.width
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.smartregister.fct.device.ui.DeviceManagerView
import org.smartregister.fct.engine.data.viewmodel.RightNavigationViewModel

@Composable
fun RightWindow(rightNavViewModel: RightNavigationViewModel) {
    Row(modifier = Modifier.fillMaxHeight()) {

        val showDeviceManagerWindow by rightNavViewModel.getDeviceManagerWindowState().collectAsState(initial = false)
        if(showDeviceManagerWindow) {
            Row(modifier = Modifier.width(400.dp).fillMaxHeight()) {
                VerticalDivider()
                DeviceManagerView()
            }
        }
    }
}