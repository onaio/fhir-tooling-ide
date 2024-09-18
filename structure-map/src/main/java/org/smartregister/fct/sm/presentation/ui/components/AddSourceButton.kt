package org.smartregister.fct.sm.presentation.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.smartregister.fct.aurora.presentation.ui.components.TextButton
import org.smartregister.fct.common.data.controller.DialogController
import org.smartregister.fct.common.presentation.ui.dialog.DialogType
import org.smartregister.fct.common.presentation.ui.dialog.rememberDialog
import org.smartregister.fct.device_database.ui.presentation.dialog.rememberDBDataProviderDialog
import org.smartregister.fct.editor.data.enums.FileType
import org.smartregister.fct.fm.domain.model.FPInitialConfig
import org.smartregister.fct.fm.presentation.ui.dialog.rememberFileProviderDialog
import org.smartregister.fct.sm.presentation.component.TabComponent

@Composable
internal fun AddSourceButton(component: TabComponent) {
    val scope = rememberCoroutineScope()
    val smDetail = component.smDetail
    val sourceName by component.sourceName.subscribeAsState()

    val parsingErrorDialog = parseErrorDialog()

    val filePickerDialog = fileProviderDialog(
        scope = scope,
        component = component,
        parsingErrorDialog = parsingErrorDialog
    )

    val dbDataProviderDialog = dbDataProviderDialog(
        scope = scope,
        component = component,
        parsingErrorDialog = parsingErrorDialog
    )

    val expanded = remember { mutableStateOf(false) }

    Box {
        TextButton(
            modifier = Modifier.fillMaxWidth(),
            label = sourceName,
            onClick = {
                scope.launch {
                    expanded.value = !expanded.value
                }
            }
        )

        ResourceProviderPopupMenu(
            expanded = expanded,
            onSelected = {
                expanded.value = false
                when (it) {
                    ProviderType.File -> {
                        filePickerDialog.show(
                            FPInitialConfig(
                                title = sourceName,
                                initialData = smDetail.source ?: ""
                            )
                        )
                    }

                    ProviderType.Database -> {
                        dbDataProviderDialog.show()
                    }
                }
            }
        )
    }
}

context (BoxScope)
@Composable
private fun ResourceProviderPopupMenu(
    expanded: MutableState<Boolean>,
    onSelected: (ProviderType) -> Unit
) {

    DropdownMenu(
        expanded = expanded.value,
        onDismissRequest = { expanded.value = false }
    ) {
        DropdownMenuItem(
            text = {
                Text("File Data Provider")
            },
            onClick = {
                onSelected(ProviderType.File)
            }
        )
        DropdownMenuItem(
            text = {
                Text("Database Data Provider")
            },
            onClick = {
                onSelected(ProviderType.Database)
            }
        )
    }
}

private enum class ProviderType {
    File, Database
}

@Composable
private fun parseErrorDialog() = rememberDialog<String>(
    title = "Parsing Error",
    width = 300.dp,
    dialogType = DialogType.Error
) { _, errorMessage ->
    Text(
        modifier = Modifier.fillMaxWidth().padding(12.dp),
        text = errorMessage ?: "Unknown Error",
        textAlign = TextAlign.Center
    )
}

@Composable
private fun fileProviderDialog(
    scope: CoroutineScope,
    component: TabComponent,
    parsingErrorDialog: DialogController<String>
) = rememberFileProviderDialog(
    componentContext = component,
    fileType = FileType.Json
) { source ->

    scope.launch {
        val result = component.addSource(source)
        if (result.isFailure) {
            parsingErrorDialog.show(result.exceptionOrNull()?.message)
        }
    }
}

@Composable
private fun dbDataProviderDialog(
    scope: CoroutineScope,
    component: TabComponent,
    parsingErrorDialog: DialogController<String>
) = rememberDBDataProviderDialog(
    componentContext = component,
) { source ->

    scope.launch {
        val result = component.addSource(source)
        if (result.isFailure) {
            parsingErrorDialog.show(result.exceptionOrNull()?.message)
        }
    }
}