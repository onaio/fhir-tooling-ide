package org.smartregister.fct.logcat.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.smartregister.fct.common.domain.model.ViewMode
import org.smartregister.fct.logcat.ui.components.LogContainer
import org.smartregister.fct.logcat.ui.components.TopBar

@Composable
fun LogcatWindow(
    modifier: Modifier = Modifier,
    onViewModeSelected: (ViewMode) -> Unit,
) {
    Column(
        modifier = modifier.fillMaxSize()
    ) {
        TopBar(
            onViewModeSelected = onViewModeSelected
        )
        HorizontalDivider()
        LogContainer()
    }
}








