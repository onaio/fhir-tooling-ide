package org.smartregister.fct.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.smartregister.fct.engine.data.enums.BottomWindowState
import org.smartregister.fct.engine.ui.viewmodel.SubWindowViewModel
import org.smartregister.fct.logcat.ui.LogcatWindow
import org.smartregister.fct.logger.FCTLogger

@Composable
fun BottomWindow(subWindowViewModel: SubWindowViewModel) {

    val windowState by subWindowViewModel.getBottomWindowState().collectAsState(initial = null)

    if (windowState != null) {
        Column(modifier = Modifier.height(200.dp).fillMaxWidth()) {
            HorizontalDivider()

            when (windowState) {
                BottomWindowState.Logcat -> {
                    LogcatWindow(onClose = {
                        subWindowViewModel.setBottomWindowState(BottomWindowState.Logcat)
                    })
                }

                else -> FCTLogger.e(IllegalStateException("Unknown State"))
            }
        }
    }
}