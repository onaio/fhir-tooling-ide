package org.smartregister.fct.rules.presentation.ui.components

import androidx.compose.runtime.Composable
import org.smartregister.fct.aurora.AuroraIconPack
import org.smartregister.fct.aurora.auroraiconpack.FolderOpen
import org.smartregister.fct.aurora.presentation.ui.components.TooltipPosition
import org.smartregister.fct.rules.presentation.components.RulesScreenComponent
import org.smartregister.fct.rules.presentation.ui.dialog.rememberAllWorkspacesDialog

@Composable
fun ShowAllWorkspacesButton(
    component: RulesScreenComponent
) {

    val allWorkspacesDialog = rememberAllWorkspacesDialog {
        component.openWorkspace(it)
    }

    CircleButton(
        icon = AuroraIconPack.FolderOpen,
        tooltip = "Show Workspaces",
        tooltipPosition = TooltipPosition.Bottom(),
        onClick = {
            allWorkspacesDialog.show()
        }
    )
}