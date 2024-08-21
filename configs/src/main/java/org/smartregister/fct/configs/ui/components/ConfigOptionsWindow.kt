package org.smartregister.fct.configs.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.RocketLaunch
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.smartregister.fct.aurora.presentation.ui.components.Button

@Composable
fun ConfigOptionsWindow() {

    Row(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {

            Box(
                modifier = Modifier.fillMaxWidth().padding(12.dp)
            ) {
                Text(
                    text = "Configuration",
                    style = MaterialTheme.typography.titleSmall,
                )
            }
            HorizontalDivider()

            Column(
                modifier = Modifier.fillMaxWidth().padding(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Button(
                    label = "Run",
                    icon = Icons.Rounded.RocketLaunch,
                    onClick = {}
                )
            }
        }
    }
}