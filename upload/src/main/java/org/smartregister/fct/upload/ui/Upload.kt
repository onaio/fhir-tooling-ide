import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Upload
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import io.github.vinceglb.filekit.compose.PickerResultLauncher
import io.github.vinceglb.filekit.compose.rememberFilePickerLauncher
import io.github.vinceglb.filekit.core.PickerMode
import io.github.vinceglb.filekit.core.PickerType
import kotlinx.coroutines.launch
import org.smartregister.fct.editor.data.enums.CodeStyle
import org.smartregister.fct.editor.ui.CodeEditor
import org.smartregister.fct.editor.ui.rememberCodeController
import org.smartregister.fct.radiance.ui.components.Button
import org.smartregister.fct.radiance.ui.components.ButtonType
import org.smartregister.fct.radiance.ui.components.OutlinedButton
import org.smartregister.fct.radiance.ui.components.TextButton
import org.smartregister.fct.radiance.ui.components.dialog.rememberDialogController
import org.smartregister.fct.upload.domain.model.FileResult

private val supportedExtension = listOf("json", "map")

@Composable
fun rememberFilePicker(
    extensions: List<String> = supportedExtension,
    onResult: (FileResult) -> Unit
): PickerResultLauncher {

    val scope = rememberCoroutineScope()
    return rememberFilePickerLauncher(
        type = PickerType.File(
            extensions = extensions
        ),
        mode = PickerMode.Single,
    ) {
        scope.launch {
            it?.readBytes()?.inputStream()?.bufferedReader()?.use { reader ->
                onResult(
                    FileResult(
                        text = reader.readText(),
                        it
                    )
                )
            }
        }
    }
}

@Composable
fun UploadButton(
    modifier: Modifier = Modifier,
    text: String = "Upload",
    icon: ImageVector? = null,
    extensions: List<String> = supportedExtension,
    buttonType: ButtonType = ButtonType.Button,
    onResult: (FileResult) -> Unit
) {

    val filePicker = rememberFilePicker(extensions, onResult)
    val buttonClick = { filePicker.launch() }

    when (buttonType) {
        ButtonType.Button -> Button(
            modifier = modifier,
            label = text,
            icon = icon,
            onClick = buttonClick
        )

        ButtonType.TextButton -> TextButton(
            modifier = modifier,
            label = text,
            icon = icon,
            onClick = buttonClick
        )

        ButtonType.OutlineButton -> OutlinedButton(
            modifier = modifier,
            label = text,
            icon = icon,
            onClick = buttonClick
        )
    }
}

@Composable
fun UploadFromInputFieldWithFab(
    modifier: Modifier = Modifier,
    initial: String = "",
    codeStyle: CodeStyle,
    onResult: (String) -> Unit
) {

    val controller = rememberCodeController(initial)

    Scaffold(
        modifier = modifier,
        floatingActionButton = {
            FloatingActionButton(
                onClick = { onResult(controller.getText()) },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ) {
                Icon(
                    imageVector = Icons.Outlined.Upload,
                    contentDescription = null
                )
            }
        }
    ) {
        Box(modifier = Modifier.padding(it)) {
            CodeEditor(
                modifier = modifier,
                codeStyle = codeStyle,
                controller = controller
            )
        }
    }
}

@Composable
fun UploadFromInputFieldButtonWithDialog(
    modifier: Modifier = Modifier,
    title: String,
    label: String = "Upload",
    icon: ImageVector? = Icons.Outlined.Upload,
    initial: String = "",
    codeStyle: CodeStyle,
    buttonType: ButtonType = ButtonType.Button,
    dialogWidth: Dp = 1200.dp,
    dialogHeight: Dp = 700.dp,
    onResult: (String) -> Unit
) {

    val dialogController = rememberDialogController(
        title = title,
        width = dialogWidth,
        height = dialogHeight,
    ) {controller ->

        UploadFromInputFieldWithFab(
            initial = initial,
            codeStyle = codeStyle,
        ) {
            controller.hide()
            onResult(it)
        }
    }

    val clickShowDialog = { dialogController.show() }

    when (buttonType) {
        ButtonType.Button -> Button(
            modifier = modifier,
            label = label,
            icon = icon,
            onClick = clickShowDialog
        )
        ButtonType.TextButton -> TextButton(
            modifier = modifier,
            label = label,
            icon = icon,
            onClick = clickShowDialog
        )
        ButtonType.OutlineButton -> OutlinedButton(
            modifier = modifier,
            label = label,
            icon = icon,
            onClick = clickShowDialog
        )
    }

}