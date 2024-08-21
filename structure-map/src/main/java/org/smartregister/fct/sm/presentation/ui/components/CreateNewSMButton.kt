package org.smartregister.fct.sm.presentation.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import org.smartregister.fct.aurora.presentation.ui.components.FloatingActionIconButton
import org.smartregister.fct.aurora.presentation.ui.components.TextButton
import org.smartregister.fct.aurora.presentation.ui.components.dialog.rememberSingleFieldDialog
import org.smartregister.fct.aurora.util.fileNameValidation
import org.smartregister.fct.editor.data.enums.FileType
import org.smartregister.fct.common.util.uuid
import org.smartregister.fct.fm.domain.model.FPInitialConfig
import org.smartregister.fct.fm.presentation.ui.dialog.rememberFileProviderDialog
import org.smartregister.fct.sm.domain.model.SMDetail
import org.smartregister.fct.sm.presentation.component.StructureMapScreenComponent

@Composable
fun CreateNewSMButton(
    modifier: Modifier = Modifier,
    label: String? = "New Structure Map",
    icon: ImageVector? = Icons.Outlined.Add,
    component: StructureMapScreenComponent
) {

    val smData = remember { mutableStateOf("") }
    val smTitleDialog = rememberSingleFieldDialog(
        title = "Structure Map Title",
        maxLength = 30,
        validations = listOf(fileNameValidation)
    ) { title, _ ->
        component.insertNewStructureMap(
            SMDetail(
                id = uuid(),
                title = title,
                body = smData.value,
            )
        )
        smData.value = ""
    }

    val fileProviderDialog = rememberFileProviderDialog(
        componentContext = component,
        fileType = FileType.StructureMap
    ) {
        smData.value = it
        smTitleDialog.show()
    }

    if (label != null) {
        TextButton(
            modifier = modifier,
            label = label,
            icon = icon,
        ) {
            fileProviderDialog.show(
                FPInitialConfig(
                    title = "New Map",
                    initialData = ""
                )
            )
        }
    } else {
        FloatingActionIconButton(
            containerColor = MaterialTheme.colorScheme.tertiaryContainer,
            icon = icon!!,
            onClick = {
                fileProviderDialog.show(
                    FPInitialConfig(
                        title = "New Map",
                        initialData = ""
                    )
                )
            }
        )
    }

}