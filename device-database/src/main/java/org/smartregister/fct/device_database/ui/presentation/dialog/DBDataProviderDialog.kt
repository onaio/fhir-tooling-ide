package org.smartregister.fct.device_database.ui.presentation.dialog

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.ComponentContext
import org.smartregister.fct.common.data.controller.DialogController
import org.smartregister.fct.common.presentation.ui.dialog.rememberDialog
import org.smartregister.fct.device_database.ui.components.QueryTabComponent
import org.smartregister.fct.device_database.ui.presentation.components.QueryTabPanel

@Composable
fun rememberDBDataProviderDialog(
    componentContext: ComponentContext,
    title: String = "Database Data Provider",
    onDismiss: ((DialogController<String>) -> Unit)? = null,
    onDataSelect: (String) -> Unit
): DialogController<String> {

    val dialogController = rememberDialog(
        width = 1200.dp,
        height = 800.dp,
        title = title,
        onDismiss = onDismiss,
    ) { controller, _ ->

        DBDataProviderDialog(
            controller = controller,
            componentContext = componentContext,
            onDataSelect = onDataSelect
        )
    }

    return dialogController
}

@Composable
private fun DBDataProviderDialog(
    controller: DialogController<String>,
    componentContext: ComponentContext,
    onDataSelect: (String) -> Unit
) {
    val queryTabComponent = remember { QueryTabComponent(componentContext) }
    QueryTabPanel(
        tabComponent = queryTabComponent,
        componentContext = queryTabComponent,
        onDataSelect = {
            controller.hide()
            onDataSelect(it)
        }
    )
}
