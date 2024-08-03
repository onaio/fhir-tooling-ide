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
import org.smartregister.fct.engine.data.viewmodel.WindowViewModel
import org.smartregister.fct.logcat.FCTLogger
import org.smartregister.fct.logcat.ui.LogcatWindow

@Composable
fun BottomWindow(viewModel: WindowViewModel) {

    val windowState by viewModel.getBottomWindowState().collectAsState(initial = null)

    if (windowState != null) {
        Column(modifier = Modifier.height(200.dp).fillMaxWidth()) {
            HorizontalDivider()

            when(windowState) {
                BottomWindowState.Logcat -> {
                    LogcatWindow(
                        onClose = {
                            viewModel.setBottomWindowState(BottomWindowState.Logcat)
                        }
                    )
                }
                else -> FCTLogger.e(IllegalStateException("Unknown State"))
            }
        }
    }
}