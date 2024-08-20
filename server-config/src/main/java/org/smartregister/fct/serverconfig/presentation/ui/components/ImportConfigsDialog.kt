package org.smartregister.fct.serverconfig.presentation.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import org.smartregister.fct.aurora.ui.components.Button
import org.smartregister.fct.aurora.ui.components.dialog.DialogType
import org.smartregister.fct.aurora.ui.components.dialog.rememberAlertDialog
import org.smartregister.fct.aurora.ui.components.dialog.rememberDialog
import org.smartregister.fct.common.domain.model.ServerConfig
import org.smartregister.fct.editor.data.enums.FileType
import org.smartregister.fct.fm.presentation.ui.dialog.rememberFileProviderDialog
import org.smartregister.fct.serverconfig.domain.model.ImportDialogState
import org.smartregister.fct.serverconfig.presentation.components.ImportConfigDialogComponent
import org.smartregister.fct.serverconfig.presentation.components.ServerConfigPanelComponent

@Composable
internal fun ServerConfigPanelComponent.ImportConfigsDialog() {

    val state by importDialogState.subscribeAsState()

    val fileProviderDialog = rememberFileProviderDialog(
        componentContext = this,
        title = "Import Server Configs",
        fileType = FileType.Json,
        onFileContent = {
            (state as ImportDialogState.ImportFileDialog).component.loadConfigs(it)
        }
    )

    val importErrorDialog = rememberAlertDialog(
        title = "Import Error",
        dialogType = DialogType.Error,
    )

    val importConfigDialog = rememberDialog<Unit>(
        title = "Import Configs",
        width = 300.dp,
        height = 500.dp,
        onDismiss = { hideExportConfigDialog() }
    ) { _, _ ->

        if (state is ImportDialogState.SelectConfigsDialog) {
            val selectConfigState = state as ImportDialogState.SelectConfigsDialog
            with(selectConfigState.component) {
                Content(selectConfigState.configs)
            }
        }
    }

    when (val s = state) {
        is ImportDialogState.ImportFileDialog -> {
            fileProviderDialog.show()
        }
        is ImportDialogState.SelectConfigsDialog -> {
            importConfigDialog.show()
        }
        is ImportDialogState.ImportErrorDialog -> {
            importErrorDialog.show(s.error)
        }
        else -> {
            fileProviderDialog.hide()
            importConfigDialog.hide()
            importErrorDialog.hide()
        }
    }
}

context (ImportConfigDialogComponent, ServerConfigPanelComponent)
@Composable
private fun Content(configs: List<ServerConfig>) {
    ConstraintLayout {
        val (list, button) = createRefs()

        Box(Modifier.padding(horizontal = 12.dp).constrainAs(list) {
            start.linkTo(parent.start)
            top.linkTo(parent.top)
            end.linkTo(parent.end)
            bottom.linkTo(button.top)
            height = Dimension.preferredWrapContent
        }) {
            ConfigList(configs)
        }

        Box(
            Modifier.background(MaterialTheme.colorScheme.surfaceContainer).padding(12.dp)
                .constrainAs(button) {
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom)
                }) {
            ExportButton()
        }
    }
}

context (ImportConfigDialogComponent, ServerConfigPanelComponent, BoxScope)
@Composable
private fun ConfigList(configs: List<ServerConfig>) {

    // TODO scrollbar indicator not showing right now will need to figure out
    LazyColumn(Modifier.fillMaxSize()) {

        items(configs) { item ->

            var isChecked by remember { mutableStateOf(false) }

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = isChecked,
                    onCheckedChange = {
                        isChecked = it
                        addOrRemoveConfig(it, item)
                    }
                )
                Spacer(Modifier.width(8.dp))
                Text(item.title)
            }
        }
    }

}

context (ImportConfigDialogComponent, ServerConfigPanelComponent)
@Composable
private fun ExportButton() {

    Button(
        modifier = Modifier.fillMaxWidth(),
        label = "Import",
        enable = checkedConfigs.subscribeAsState().value.isNotEmpty(),
        onClick = ::import
    )

}