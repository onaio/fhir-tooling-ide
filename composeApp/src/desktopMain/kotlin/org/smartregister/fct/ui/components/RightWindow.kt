package org.smartregister.fct.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.smartregister.fct.device.ui.DeviceManagerView

@Composable
fun RightWindow() {
    Row(modifier = Modifier.fillMaxHeight()) {
        VerticalDivider()
        Column(modifier = Modifier.fillMaxHeight()) {
            DeviceManagerView()
        }
    }
}