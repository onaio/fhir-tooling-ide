package org.smartregister.fct.sm.ui.components

import UploadButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Upload
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import org.koin.java.KoinJavaComponent.getKoin
import org.smartregister.fct.engine.util.uuid
import org.smartregister.fct.aurora.ui.components.ButtonType
import org.smartregister.fct.aurora.ui.components.dialog.rememberSingleFieldDialog
import org.smartregister.fct.sm.data.viewmodel.SMViewModel
import org.smartregister.fct.sm.domain.model.SMDetail

@Composable
fun SMUploadButton(
    modifier: Modifier = Modifier,
    label: String = "Upload Structure Map",
    buttonType: ButtonType = ButtonType.Button,
    icon: ImageVector? = Icons.Outlined.Upload
) {

    val smViewModel = getKoin().get<SMViewModel>()
    val smData = remember { mutableStateOf("") }
    val singleFieldDialog = rememberSingleFieldDialog(
        title = "Structure Map Title",
        maxLength = 30
    ) { title ->
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

    UploadButton(
        modifier = modifier,
        text = label,
        icon = icon,
        extensions = listOf("map"),
        buttonType = buttonType,
    ) {
        smData.value = it.text
        singleFieldDialog.show()
    }

}