package org.smartregister.fct.presentation.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.smartregister.fct.common.data.enums.BottomWindowState
import org.smartregister.fct.common.data.manager.SubWindowManager
import org.smartregister.fct.logcat.ui.LogcatWindow
import org.smartregister.fct.logger.FCTLogger

@Composable
fun BottomWindow(subWindowManager: SubWindowManager) {

    val windowState by subWindowManager.getBottomWindowState().collectAsState(initial = null)

    if (windowState != null) {
        Column(modifier = Modifier.height(200.dp).fillMaxWidth()) {
            HorizontalDivider()

            when (windowState) {
                BottomWindowState.Logcat -> {
                    LogcatWindow(onClose = {
                        subWindowManager.setBottomWindowState(BottomWindowState.Logcat)
                    })
                }

                else -> FCTLogger.e(IllegalStateException("Unknown State"))
            }
        }
    }
}