package org.smartregister.fct.fhirman.presentation.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch
import org.smartregister.fct.common.data.controller.SingleFieldDialogController
import org.smartregister.fct.common.presentation.ui.dialog.rememberSingleFieldDialog
import org.smartregister.fct.common.util.fileNameValidation
import org.smartregister.fct.fhirman.data.manager.FhirmanServerTabsManager
import org.smartregister.fct.fhirman.presentation.components.FhirmanServerComponent

context (FhirmanServerTabsManager)
@Composable
internal fun FhirmanServerComponent.NewServerTabWrapper(
    content: @Composable (SingleFieldDialogController) -> Unit
) {
    val scope = rememberCoroutineScope()
    val tabTitleDialogController = rememberSingleFieldDialog(
        title = "Tab Title",
        validations = listOf(fileNameValidation)
    ) { text, _ ->
        scope.launch {
            addNewTab(this@NewServerTabWrapper, text)
        }
    }

    content(tabTitleDialogController)
}