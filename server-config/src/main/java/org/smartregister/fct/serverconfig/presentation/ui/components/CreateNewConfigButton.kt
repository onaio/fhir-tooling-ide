package org.smartregister.fct.serverconfig.presentation.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.runtime.Composable
import org.smartregister.fct.aurora.domain.controller.SingleFieldDialogController
import org.smartregister.fct.aurora.ui.components.TextButton

@Composable
internal fun CreateNewConfigButton(titleDialogController: SingleFieldDialogController) {

    TextButton(
        label = "Create New",
        icon = Icons.Outlined.Add,
        onClick = {
            titleDialogController.show()
        }
    )
}