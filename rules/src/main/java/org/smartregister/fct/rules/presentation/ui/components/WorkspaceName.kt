package org.smartregister.fct.rules.presentation.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Chip
import androidx.compose.material.ChipDefaults
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.smartregister.fct.rules.presentation.components.RulesScreenComponent

@OptIn(ExperimentalMaterialApi::class)
@Composable
internal fun BoxScope.WorkspaceName(
    component: RulesScreenComponent,
) {
    val activeWorkspace by component.workspace.collectAsState()

    if (activeWorkspace != null) {
        Chip(
            modifier = Modifier.align(Alignment.TopCenter).padding(top = 12.dp),
            border = BorderStroke(
                width = 0.5.dp,
                color = colorScheme.onSurface.copy(0.6f)
            ),
            onClick = {},
            colors = ChipDefaults.chipColors(
                backgroundColor = colorScheme.surface,
            ),
        ) {
            Text(
                text = activeWorkspace!!.name,
                color = colorScheme.onSurface
            )
        }
    }

}