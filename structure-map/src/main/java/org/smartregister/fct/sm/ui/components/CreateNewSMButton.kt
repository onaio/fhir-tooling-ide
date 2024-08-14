package org.smartregister.fct.sm.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import org.koin.java.KoinJavaComponent.getKoin
import org.smartregister.fct.engine.util.uuid
import org.smartregister.fct.aurora.ui.components.ButtonType
import org.smartregister.fct.aurora.ui.components.TextButton
import org.smartregister.fct.aurora.ui.components.dialog.rememberSingleFieldDialog
import org.smartregister.fct.aurora.util.fileNameValidation
import org.smartregister.fct.aurora.util.folderNameValidation
import org.smartregister.fct.editor.data.enums.FileType
import org.smartregister.fct.fm.ui.dialog.rememberFileProviderDialog
import org.smartregister.fct.sm.data.viewmodel.SMViewModel
import org.smartregister.fct.sm.domain.model.SMDetail

@Composable
fun CreateNewSMButton(
    modifier: Modifier = Modifier,
    label: String = "Create Structure Map",
    icon: ImageVector? = Icons.Outlined.Add
) {

    val smViewModel = getKoin().get<SMViewModel>()
    val smData = remember { mutableStateOf("") }
    val smTitleDialog = rememberSingleFieldDialog(
        title = "Structure Map Title",
        maxLength = 30,
        validations = listOf(fileNameValidation)
    ) { title, _ ->
        smViewModel.insert(
            SMDetail(
                id = uuid(),
                title = title,
                body = smData.value,
                source = ""
            )
        )
        smData.value = ""
    }

    val fileProviderDialog = rememberFileProviderDialog(
        fileType = FileType.StructureMap
    ) {
        smData.value = it
        smTitleDialog.show()
    }

    TextButton(
        modifier = modifier,
        label = label,
        icon = icon,
    ) {
        fileProviderDialog.show("New Map")
    }

}