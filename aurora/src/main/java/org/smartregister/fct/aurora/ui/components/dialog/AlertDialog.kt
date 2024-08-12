package org.smartregister.fct.aurora.ui.components.dialog

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import org.smartregister.fct.aurora.domain.controller.DialogController

@Composable
fun rememberAlertDialog(
    title: String,
    width: Dp = 300.dp,
    height: Dp? = null,
    cancelable: Boolean = true,
    content: @Composable (ColumnScope.(DialogController) -> Unit)
): DialogController {

    val isShowDialog = remember { mutableStateOf(false) }
    var dialogType by remember { mutableStateOf(DialogType.Default) }

    val dialogController = remember {
        DialogController(
            onShow = {
                dialogType = it
                isShowDialog.value = true
            },
            onHide = { isShowDialog.value = false }
        )
    }

    Dialog(
        dialogController = dialogController,
        isShowDialog = isShowDialog,
        dialogType = dialogType,
        title = title,
        width = width,
        height = height ?: 150.dp,
        cancelable = cancelable,
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            content = {
                content(it)
            }
        )
    }

    return dialogController
}