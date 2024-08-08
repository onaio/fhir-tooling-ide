package org.smartregister.fct.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.smartregister.fct.configs.ui.components.ConfigOptionsWindow
import org.smartregister.fct.engine.data.enums.LeftWindowState
import org.smartregister.fct.engine.data.viewmodel.SubWindowViewModel
import org.smartregister.fct.logcat.FCTLogger
import org.smartregister.fct.sm.ui.components.SMOptionWindow

@Composable
fun LeftWindow(subWindowViewModel: SubWindowViewModel) {

    val windowState by subWindowViewModel.getLeftWindowState().collectAsState(initial = null)
    if (windowState != null) {
        Row(
            modifier = Modifier
                .width(190.dp)
                .fillMaxHeight()
                .background(MaterialTheme.colorScheme.surfaceContainer)
        ) {
            Box(Modifier.fillMaxSize()) {
                when (windowState) {
                    LeftWindowState.Configs -> ConfigOptionsWindow()
                    LeftWindowState.StructureMap -> SMOptionWindow()
                    else -> FCTLogger.e(IllegalStateException("Unknown State"))
                }
            }
            VerticalDivider()
        }
    }

}