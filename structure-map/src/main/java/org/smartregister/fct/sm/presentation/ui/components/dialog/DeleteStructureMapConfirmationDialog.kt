package org.smartregister.fct.sm.presentation.ui.components.dialog

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import org.smartregister.fct.aurora.ui.components.dialog.rememberConfirmationDialog
import org.smartregister.fct.sm.presentation.component.StructureMapScreenComponent

@Composable
fun DeleteStructureMapConfirmationDialog(component: StructureMapScreenComponent) {

    val showDialog by component.showStructureMapDeleteDialog.subscribeAsState()

    val confirmationDialog = rememberConfirmationDialog<Int>(
        title = "Delete Structure Map",
        message = "Are you sure you want to delete this structure-map?",
        onDismiss = { component.hideDeleteStructureMapDialog() },
        onConfirmed = { component.closeTab() }
    )

    if (showDialog.first) {
        confirmationDialog.show()
    }
}