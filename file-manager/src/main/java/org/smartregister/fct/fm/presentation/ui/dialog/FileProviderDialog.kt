package org.smartregister.fct.fm.presentation.ui.dialog

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.ComponentContext
import fct.file_manager.generated.resources.Res
import fct.file_manager.generated.resources.in_app_file_manager_heading
import fct.file_manager.generated.resources.system_file_manager_heading
import okio.Path
import org.jetbrains.compose.resources.stringResource
import org.smartregister.fct.aurora.domain.controller.DialogController
import org.smartregister.fct.aurora.ui.components.FloatingActionIconButton
import org.smartregister.fct.aurora.ui.components.Tabs
import org.smartregister.fct.aurora.ui.components.dialog.rememberDialog
import org.smartregister.fct.editor.data.controller.CodeController
import org.smartregister.fct.editor.data.enums.FileType
import org.smartregister.fct.editor.ui.CodeEditor
import org.smartregister.fct.fm.domain.model.FileManagerMode
import org.smartregister.fct.fm.domain.model.FPInitialConfig
import org.smartregister.fct.fm.presentation.ui.components.InAppFileManager
import org.smartregister.fct.fm.presentation.ui.components.SystemFileManager

@Composable
fun rememberFileProviderDialog(
    componentContext: ComponentContext,
    title: String = "File Provider",
    fileType: FileType? = null,
    onDismiss: ((DialogController<FPInitialConfig>) -> Unit)? = null,
    onFilePath: ((Path) -> Unit)? = null,
    onFileContent: ((String) -> Unit)? = null
): DialogController<FPInitialConfig> {

    val scope = rememberCoroutineScope()

    val dialogController = rememberDialog(
        width = 1200.dp,
        height = 800.dp,
        title = title,
        onDismiss = onDismiss,
    ) { controller, config ->

        val editorTitle = config?.title ?: "Untitled"
        val initialData = config?.initialData ?: ""
        val codeController = CodeController(scope, initialData, fileType)

        FileProviderDialog(
            editorTitle = editorTitle,
            onFilePath = onFilePath,
            onFileContent = onFileContent,
            fileProviderController = controller,
            codeController = codeController,
            componentContext = componentContext
        )
    }

    return dialogController
}

@Composable
private fun FileProviderDialog(
    editorTitle: String,
    onFilePath: ((Path) -> Unit)? = null,
    onFileContent: ((String) -> Unit)? = null,
    fileProviderController: DialogController<FPInitialConfig>,
    codeController: CodeController,
    componentContext: ComponentContext
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
                ) else SystemFileManager(componentContext, mode)

                1 -> if (initialData.isNotEmpty()) SystemFileManager(
                    componentContext,
                    mode
                ) else InAppFileManager(
                    componentContext, mode
                )

                2 -> if (initialData.isNotEmpty()) InAppFileManager(
                    componentContext,
                    mode
                ) else CodeView(
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
    fileProviderController: DialogController<FPInitialConfig>
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