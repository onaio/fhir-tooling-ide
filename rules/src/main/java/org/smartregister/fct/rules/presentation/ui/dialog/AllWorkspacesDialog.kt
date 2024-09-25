package org.smartregister.fct.rules.presentation.ui.dialog

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.flow.collectLatest
import org.koin.compose.koinInject
import org.smartregister.fct.aurora.presentation.ui.components.SmallIconButton
import org.smartregister.fct.aurora.presentation.ui.components.TooltipPosition
import org.smartregister.fct.aurora.util.doubleClick
import org.smartregister.fct.common.data.controller.DialogController
import org.smartregister.fct.common.presentation.ui.dialog.rememberConfirmationDialog
import org.smartregister.fct.common.presentation.ui.dialog.rememberDialog
import org.smartregister.fct.rules.domain.model.Workspace
import org.smartregister.fct.rules.domain.usecase.DeleteWorkspace
import org.smartregister.fct.rules.domain.usecase.GetAllWorkspace
import org.smartregister.fct.rules.util.WorkspaceConfig

@Composable
internal fun rememberAllWorkspacesDialog(
    title: String = "All Workspaces",
    onDismiss: ((DialogController<Workspace>) -> Unit)? = null,
    onDone: (Workspace) -> Unit
): DialogController<Workspace> {

    val scope = rememberCoroutineScope()

    val dialogController = rememberDialog(
        width = 600.dp,
        height = 700.dp,
        title = title,
        onDismiss = onDismiss,
    ) { controller, existingWorkspace ->

        AllWorkspacesDialog(
            controller = controller,
            existingWorkspace = existingWorkspace,
            onDone = onDone,
        )
    }

    return dialogController
}

@Composable
private fun AllWorkspacesDialog(
    controller: DialogController<Workspace>,
    existingWorkspace: Workspace?,
    onDone: (Workspace) -> Unit,
) {

    val getAllWorkspaces = koinInject<GetAllWorkspace>()
    val deleteWorkspace = koinInject<DeleteWorkspace>()

    val scope = rememberCoroutineScope()
    var allWorkspaceList by remember { mutableStateOf<List<Workspace>>(listOf()) }

    val deleteWorkspaceDialog = rememberConfirmationDialog<Workspace>{ _, workspace ->
        deleteWorkspace(workspace!!.id)
    }

    LaunchedEffect(Unit) {
        getAllWorkspaces().collectLatest {
            allWorkspaceList = it
        }
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {

        itemsIndexed(allWorkspaceList) { index, item ->

            if (index > 0) {
                HorizontalDivider()
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .clickable {  }
                    .doubleClick(scope) {
                        controller.hide()
                        onDone(item)
                    },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    modifier = Modifier.width(30.dp).padding(start = 12.dp),
                    text = "${index + 1}"
                )
                Row(
                    modifier = Modifier.fillMaxWidth().padding(start = 4.dp, end = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = item.name
                    )
                    if (item.id != WorkspaceConfig.workspace?.id) {
                        SmallIconButton(
                            icon = Icons.Outlined.Delete,
                            onClick = {
                                deleteWorkspaceDialog.show(
                                    title = "Delete Workspace",
                                    message = "Are you sure you want to delete this workspace?",
                                    data = item
                                )
                            },
                            tooltip = "Delete",
                            tooltipPosition = TooltipPosition.Bottom()
                        )
                    }
                }
            }
        }

        if (allWorkspaceList.isEmpty()) {
            item {
                Text(
                    modifier = Modifier.fillMaxWidth().padding(12.dp),
                    text = "No workspace available",
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}