package org.smartregister.fct.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.width
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.smartregister.fct.device.ui.DeviceManagerWindow
import org.smartregister.fct.engine.data.enums.RightWindowState
import org.smartregister.fct.engine.data.viewmodel.SubWindowViewModel
import org.smartregister.fct.logcat.FCTLogger
import org.smartregister.fct.pm.ui.PackageManagerWindow

@Composable
fun RightWindow(subWindowViewModel: SubWindowViewModel) {

    val windowState by subWindowViewModel.getRightWindowState().collectAsState(initial = null)
    if (windowState != null) {
        Row(modifier = Modifier.width(400.dp).fillMaxHeight()) {
            VerticalDivider()
            when (windowState) {
                RightWindowState.DeviceManager -> DeviceManagerWindow()
                RightWindowState.PackageManager -> PackageManagerWindow()
                else -> FCTLogger.e(IllegalStateException("Unknown State"))
            }
        }
    }

}