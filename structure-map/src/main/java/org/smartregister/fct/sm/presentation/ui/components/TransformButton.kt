package org.smartregister.fct.sm.presentation.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.hl7.fhir.r4.model.Bundle
import org.smartregister.fct.aurora.ui.components.OutlinedButton
import org.smartregister.fct.aurora.ui.components.dialog.DialogType
import org.smartregister.fct.aurora.ui.components.dialog.rememberDialog
import org.smartregister.fct.aurora.ui.components.dialog.rememberLoaderDialogController
import org.smartregister.fct.aurora.util.getOrDefault
import org.smartregister.fct.sm.presentation.component.TabComponent

@Composable
internal fun TransformButton(component: TabComponent) {

    val scope = rememberCoroutineScope()

    val loaderController = rememberLoaderDialogController()

    val errorDialogController = rememberDialog(
        title = "Transformation Error",
        dialogType = DialogType.Error
    ) {
        Text(
            modifier = Modifier.fillMaxWidth().padding(12.dp),
            text = it.getExtra().getOrDefault(0, ""),
            textAlign = TextAlign.Center
        )
    }

    val resultDialogController = rememberDialog(
        title = "Transformation Result",
        width = 1200.dp,
        height = 800.dp,
    ) {
        SMTransformationResult(
            componentContext = component,
            bundle = it.getExtra().getOrDefault(0, Bundle())
        )
    }

    OutlinedButton(
        modifier = Modifier.fillMaxWidth(),
        label = "Transform",
        onClick = {

            loaderController.show()

            scope.launch(Dispatchers.IO) {
                val result = component.applyTransformation()
                loaderController.hide()
                if (result.isSuccess) {
                    resultDialogController.show(result.getOrThrow())
                } else {
                    errorDialogController.show(result.exceptionOrNull()?.message)
                }
            }
        }
    )

}