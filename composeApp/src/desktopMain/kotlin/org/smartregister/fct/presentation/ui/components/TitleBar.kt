package org.smartregister.fct.presentation.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.ComponentContext
import org.smartregister.fct.common.data.manager.SubWindowManager

@Composable
fun TitleBar(
    componentContext: ComponentContext,
    subWindowManager: SubWindowManager
) {

    Column(modifier = Modifier.background(MaterialTheme.colorScheme.surface).fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {

                FhirAnimatedIcon()
                Spacer(Modifier.width(18.dp))
                DeviceSelectionMenu()
                Spacer(Modifier.width(10.dp))
                ActivePackageChip(subWindowManager = subWindowManager)
            }

            Row(
                modifier = Modifier.padding(end = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End
            ) {
                TextViewerButton(componentContext)
                Spacer(Modifier.width(16.dp))
                SettingButton(componentContext)
            }
        }
    }
}