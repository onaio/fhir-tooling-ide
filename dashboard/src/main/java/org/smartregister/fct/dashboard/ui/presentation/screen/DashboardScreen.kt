package org.smartregister.fct.dashboard.ui.presentation.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import org.smartregister.fct.dashboard.ui.presentation.components.DashboardScreenComponent

@Composable
fun DashboardScreen(component: DashboardScreenComponent) {
    Box(Modifier.fillMaxSize()) {
        Text(
            modifier = Modifier.align(Alignment.Center),
            text = "..:: Dashboard ::.."
        )
    }
}