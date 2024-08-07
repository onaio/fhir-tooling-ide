import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Upload
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import io.github.vinceglb.filekit.compose.PickerResultLauncher
import io.github.vinceglb.filekit.compose.rememberFilePickerLauncher
import io.github.vinceglb.filekit.core.PickerMode
import io.github.vinceglb.filekit.core.PickerType
import kotlinx.coroutines.launch
import org.smartregister.fct.editor.data.enums.CodeStyle
import org.smartregister.fct.editor.ui.CodeEditor
import org.smartregister.fct.editor.ui.rememberCodeController
import org.smartregister.fct.engine.ui.components.ButtonWithIcon
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
    icon: ImageVector = Icons.Outlined.Upload,
    extensions: List<String> = supportedExtension,
    onResult: (FileResult) -> Unit
) {

    val filePicker = rememberFilePicker(extensions, onResult)

    ButtonWithIcon(
        modifier = modifier,
        text = text,
        icon = icon,
        onClick = {
            filePicker.launch()
        }
    )
}

@Composable
fun UploadFromInputFieldWithFab(
    modifier: Modifier = Modifier,
    codeStyle: CodeStyle,
    onResult: (String) -> Unit
) {

    val controller = rememberCodeController()

    Scaffold(
        modifier = modifier,
        floatingActionButton = {
            FloatingActionButton(
                onClick = { onResult(controller.getText()) },
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
                value = "",
                codeStyle = codeStyle,
                controller = controller
            )
        }
    }
}

@Composable
fun UploadFromInputFieldButtonWithDialog(
    modifier: Modifier = Modifier,
    text: String = "Upload",
    icon: ImageVector = Icons.Outlined.Upload,
    codeStyle: CodeStyle,
    onResult: (String) -> Unit
) {

    val isShowDialog = remember { mutableStateOf(false) }

    ButtonWithIcon(
        modifier = modifier,
        text = text,
        icon = icon,
        onClick = {
            isShowDialog.value = true
        }
    )

    if (isShowDialog.value) {
        Dialog(
            onDismissRequest = {
                isShowDialog.value = false
            },
        ) {
            UploadFromInputFieldWithFab(
                modifier = modifier.border(1.dp, MaterialTheme.colorScheme.primary)
                    .size(width = 1000.dp, height = 600.dp),
                codeStyle = codeStyle,
            ) {
                isShowDialog.value = false
                onResult(it)
            }
        }
    }
}