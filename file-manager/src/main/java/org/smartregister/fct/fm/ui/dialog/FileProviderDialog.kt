package org.smartregister.fct.fm.ui.dialog

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import fct.file_manager.generated.resources.Res
import fct.file_manager.generated.resources.in_app_file_manager_heading
import fct.file_manager.generated.resources.system_file_manager_heading
import okio.Path
import org.jetbrains.compose.resources.stringResource
import org.smartregister.fct.aurora.domain.controller.DialogController
import org.smartregister.fct.aurora.ui.components.FloatingActionIconButton
import org.smartregister.fct.aurora.ui.components.Tabs
import org.smartregister.fct.aurora.ui.components.dialog.getOrDefault
import org.smartregister.fct.aurora.ui.components.dialog.rememberDialog
import org.smartregister.fct.editor.data.controller.CodeController
import org.smartregister.fct.editor.data.enums.FileType
import org.smartregister.fct.editor.ui.CodeEditor
import org.smartregister.fct.editor.ui.rememberCodeController
import org.smartregister.fct.fm.domain.model.FileManagerMode
import org.smartregister.fct.fm.ui.components.InAppFileManager
import org.smartregister.fct.fm.ui.components.SystemFileManager

@Composable
fun rememberFileProviderDialog(
    title: String = "File Provider",
    fileType: FileType? = null,
    onCancelled: (() -> Unit)? = null,
    onFilePath: ((Path) -> Unit)? = null,
    onFileContent: ((String) -> Unit)? = null,
): DialogController {

    val scope =  rememberCoroutineScope()

    val dialogController = rememberDialog(
        width = 1200.dp,
        height = 800.dp,
        title = title,
        onCancelled = onCancelled,
    ) {
        val editorTitle = it.getExtra().getOrDefault(0, "Untitled")
        val initialData = it.getExtra().getOrDefault(1, "")
        val codeController = CodeController(scope, initialData, fileType)

        FileProviderDialog(
            editorTitle = editorTitle,
            onFilePath = onFilePath,
            onFileContent = onFileContent,
            fileProviderController = it,
            codeController = codeController
        )
    }

    return dialogController
}

@Composable
fun FileProviderDialog(
    editorTitle: String,
    onFilePath: ((Path) -> Unit)? = null,
    onFileContent: ((String) -> Unit)? = null,
    fileProviderController: DialogController,
    codeController: CodeController
) {

    val initialData = codeController.getText()

    val onFileSelected = onFileContent?.let {
        val listener: (String) -> Unit = {
            fileProviderController.hide()
            onFileContent.invoke(it)
        }
        listener
    }

    val onPathSelected = onFilePath?.let {
        val listener: (Path) -> Unit = {
            fileProviderController.hide()
            onFilePath.invoke(it)
        }
        listener
    }

    val mode = FileManagerMode.View(
        onFileSelected = onFileSelected,
        onPathSelected = onPathSelected,
        extensions = listOf(codeController.getFileType()?.extension ?: "")
    )

    Tabs(
        tabs = listOf(
            if (initialData.isNotEmpty()) editorTitle else stringResource(Res.string.system_file_manager_heading),
            if (initialData.isNotEmpty()) stringResource(Res.string.system_file_manager_heading) else stringResource(
                Res.string.in_app_file_manager_heading
            ),
            if (initialData.isNotEmpty()) stringResource(Res.string.in_app_file_manager_heading) else editorTitle,
        ),
        title = { it },
        onSelected = { tabIndex, _ ->
            when (tabIndex) {
                0 -> if (initialData.isNotEmpty()) CodeView(
                    onFileContent = onFileContent,
                    controller = codeController,
                    fileProviderController = fileProviderController
                ) else SystemFileManager(mode)

                1 -> if (initialData.isNotEmpty()) SystemFileManager(mode) else InAppFileManager(mode)
                2 -> if (initialData.isNotEmpty()) InAppFileManager(mode) else CodeView(
                    onFileContent = onFileContent,
                    controller = codeController,
                    fileProviderController = fileProviderController
                )
            }
        }
    )
}

@Composable
private fun CodeView(
    onFileContent: ((String) -> Unit)?,
    controller: CodeController,
    fileProviderController: DialogController
) {
    Scaffold(
        floatingActionButton = {
            FloatingActionIconButton(
                icon = Icons.Outlined.Check,
                onClick = {
                    fileProviderController.hide()
                    onFileContent?.invoke(controller.getText())
                },
            )
        }
    ) {
        Box(modifier = Modifier.padding(it)) {
            CodeEditor(
                controller = controller
            )
        }
    }
}