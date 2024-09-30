package org.smartregister.fct.workflow.presentation.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.smartregister.fct.aurora.AuroraIconPack
import org.smartregister.fct.aurora.auroraiconpack.Database
import org.smartregister.fct.aurora.auroraiconpack.FolderOpen
import org.smartregister.fct.aurora.presentation.ui.components.SmallIconButton
import org.smartregister.fct.aurora.presentation.ui.components.Tooltip
import org.smartregister.fct.aurora.presentation.ui.components.TooltipPosition
import org.smartregister.fct.device_database.ui.presentation.dialog.rememberDBDataProviderDialog
import org.smartregister.fct.editor.data.enums.FileType
import org.smartregister.fct.editor.ui.CodeEditor
import org.smartregister.fct.fm.presentation.ui.dialog.rememberFileProviderDialog
import org.smartregister.fct.workflow.presentation.components.BaseWorkflowComponent
import org.smartregister.fct.workflow.util.WorkflowConfig

@Composable
internal fun WorkflowEditor(component: BaseWorkflowComponent) {

    Column {
        EditorToolbar(component)
        CodeEditor(
            modifier = Modifier.fillMaxSize(),
            controller = component.codeController
        )
    }
}

@Composable
private fun EditorToolbar(component: BaseWorkflowComponent) {

    Box {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(39.dp)
                .background(MaterialTheme.colorScheme.surfaceContainer.copy(0.5f))
                .padding(horizontal = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            ImportFromFileSystem(component)
            Spacer(Modifier.width(8.dp))
            ImportFromDatabase(component)
        }

        Text(
            modifier = Modifier.align(Alignment.Center),
            text = WorkflowConfig.getFileName(component.openPath.collectAsState().value ?: "")
        )
    }
    HorizontalDivider()
}

@Composable
private fun ImportFromFileSystem(component: BaseWorkflowComponent) {

    val fileProviderDialog = rememberFileProviderDialog(
        componentContext = component,
        title = "Import",
        fileType = FileType.Json,
        onFileContent = {
            component.updateOpenedFileContent(it)
        }
    )

    Tooltip(
        tooltip = "Import From File System",
        tooltipPosition = TooltipPosition.Bottom(),
    ) {
        SmallIconButton(
            icon = AuroraIconPack.FolderOpen,
            onClick = {
                fileProviderDialog.show()
            }
        )
    }
}

@Composable
private fun ImportFromDatabase(component: BaseWorkflowComponent) {

    val dbDataProviderDialog = rememberDBDataProviderDialog(
        componentContext = component,
        title = "Import",
        defaultQuery = "SELECT * FROM ResourceEntity LIMIT 1",
    ) {
        component.updateOpenedFileContent(it)
    }

    Tooltip(
        tooltip = "Import From Database",
        tooltipPosition = TooltipPosition.Bottom(),
    ) {
        SmallIconButton(
            icon = AuroraIconPack.Database,
            onClick = {
                dbDataProviderDialog.show()
            }
        )
    }
}