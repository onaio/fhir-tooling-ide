package org.smartregister.fct.aurora.ui.components.dialog

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import org.smartregister.fct.aurora.domain.controller.DialogController

@Composable
fun <T> rememberAlertDialog(
    title: String,
    width: Dp = 300.dp,
    height: Dp? = null,
    cancelable: Boolean = true,
    dialogType: DialogType = DialogType.Default,
    onDismiss: (DialogController<T>.() -> Unit)? = null,
    content: @Composable (ColumnScope.(DialogController<T>, T?) -> Unit)
): DialogController<T> {

    val dialogController = rememberDialog(
        title = title,
        width = width,
        height = height,
        cancelable = cancelable,
        dialogType = dialogType,
        onDismiss = onDismiss,
    ) { controller, data ->
        Column(
            modifier = Modifier.fillMaxSize().padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            content = {
                content(controller, data)
            }
        )
    }

    return dialogController
}