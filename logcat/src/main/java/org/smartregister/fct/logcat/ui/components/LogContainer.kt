package org.smartregister.fct.logcat.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
internal fun LogContainer() {
    Row(
        modifier = Modifier.fillMaxSize()
    ) {
        LogConfiguration()
        VerticalDivider()
        LogWindow()
    }
}