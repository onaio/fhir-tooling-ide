package org.smartregister.fct.logcat.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.smartregister.fct.logcat.ui.components.LogContainer
import org.smartregister.fct.logcat.ui.components.TopBar

@Composable
fun LogcatWindow(
    modifier: Modifier = Modifier,
    onClose: () -> Unit,
) {
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        HorizontalDivider()
        TopBar(
            onClose = onClose
        )
        HorizontalDivider()
        LogContainer()
    }
}








