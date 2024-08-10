package org.smartregister.fct.pm.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.smartregister.fct.pm.ui.components.DeviceSelectionMenu

@Composable
fun PackageManagerWindow() {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Title()
        HorizontalDivider()
        DeviceSelectionMenu()
    }
}

@Composable
private fun Title() {
    Row(
        modifier = Modifier.fillMaxWidth()
            .background(MaterialTheme.colorScheme.surfaceContainer.copy(alpha = 0.5f))
            .padding(8.dp)
    ) {
        Text(
            text = "Package Manager",
            style = MaterialTheme.typography.titleSmall
        )
    }
}

