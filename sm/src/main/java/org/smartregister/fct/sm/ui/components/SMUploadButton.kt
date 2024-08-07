package org.smartregister.fct.sm.ui.components

import UploadButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import org.koin.java.KoinJavaComponent.getKoin
import org.smartregister.fct.engine.ui.components.rememberSingleFieldDialog
import org.smartregister.fct.engine.util.compress
import org.smartregister.fct.engine.util.uuid
import org.smartregister.fct.sm.data.viewmodel.SMViewModel
import org.smartregister.fct.sm.domain.model.SMDetail

@Composable
fun SMUploadButton(
    modifier: Modifier = Modifier
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
                body = smData.value.compress()
            )
        )
        smData.value = ""
    }

    UploadButton(
        modifier = modifier,
        text = "Upload Structure Map",
        extensions = listOf("map")
    ) {
        smData.value = it.text
        singleFieldDialog.show()
    }

}