package org.smartregister.fct.serverconfig.presentation.ui.components

import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import io.github.vinceglb.filekit.compose.rememberFileSaverLauncher
import kotlinx.coroutines.launch
import org.smartregister.fct.aurora.ui.components.Button
import org.smartregister.fct.aurora.ui.components.dialog.rememberAlertDialog
import org.smartregister.fct.aurora.ui.components.dialog.rememberDialog
import org.smartregister.fct.common.data.locals.LocalSnackbarHost
import org.smartregister.fct.serverconfig.presentation.components.ExportConfigDialogComponent
import org.smartregister.fct.serverconfig.presentation.components.ServerConfigPanelComponent

context (ExportConfigDialogComponent, ServerConfigPanelComponent)
@Composable
internal fun ExportConfigsDialog() {

    val exportConfigDialog = rememberDialog<Unit>(
        title = "Export Configs",
        width = 300.dp,
        height = 500.dp,
        onDismiss = { hideExportConfigDialog() }
    ) { _, _ ->

        ConstraintLayout {
            val (list, button) = createRefs()

            Box(Modifier.padding(horizontal = 12.dp).constrainAs(list) {
                start.linkTo(parent.start)
                top.linkTo(parent.top)
                end.linkTo(parent.end)
                bottom.linkTo(button.top)
                height = Dimension.preferredWrapContent
            }) {
                ConfigList()
            }

            Box(Modifier.background(MaterialTheme.colorScheme.surfaceContainer).padding(12.dp).constrainAs(button) {
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                bottom.linkTo(parent.bottom)
            }) {
                ExportButton()
            }
        }
    }

    exportConfigDialog.show()
}

context (ExportConfigDialogComponent, ServerConfigPanelComponent, BoxScope)
@Composable
private fun ConfigList() {

    val serverConfigs by configs.subscribeAsState()

    // TODO scrollbar indicator not showing right now will need to figure out
    LazyColumn(Modifier.fillMaxSize()) {

        items(serverConfigs) {item ->

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

context (ExportConfigDialogComponent, ServerConfigPanelComponent)
@Composable
private fun ExportButton() {

    val alertDialog = rememberAlertDialog(
        title = "Export Configs",
        message = "Configs exported successfully",
        onDismiss = {
            hideExportConfigDialog()
        }
    )
    val scope = rememberCoroutineScope()
    val jsonContent by exportedContent.subscribeAsState()

    val launcher = rememberFileSaverLauncher {
        scope.launch {
            alertDialog.show()
            exportedContent.value = ""
        }
    }

    if (jsonContent.trim().isNotEmpty()) {
        scope.launch {
            launcher.launch(
                baseName = "server_configs",
                extension = "json",
                //initialDirectory = "",
                bytes = jsonContent.encodeToByteArray()
            )
        }
    }


    Button(
        modifier = Modifier.fillMaxWidth(),
        label = "Export",
        enable = checkedConfigs.subscribeAsState().value.isNotEmpty(),
        onClick = ::export
    )

}