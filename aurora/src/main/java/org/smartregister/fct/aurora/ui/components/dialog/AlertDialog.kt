package org.smartregister.fct.aurora.ui.components.dialog

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import org.smartregister.fct.aurora.domain.controller.DialogController
import org.smartregister.fct.aurora.ui.components.SmallIconButton

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
            modifier = Modifier.fillMaxWidth().padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            content = {
                content(controller, data)
            }
        )
    }

    return dialogController
}

@Composable
fun rememberAlertDialog(
    title: String,
    message: String,
    width: Dp = 300.dp,
    height: Dp? = null,
    cancelable: Boolean = true,
    dialogType: DialogType = DialogType.Default,
    onDismiss: (DialogController<Unit>.() -> Unit)? = null,
): DialogController<Unit> {
    return rememberAlertDialog(
        title = title,
        width = width,
        height = height,
        cancelable = cancelable,
        dialogType = dialogType,
        onDismiss = onDismiss,
        content = { _, _ ->
            Text(message)
        }
    )
}

@Composable
fun rememberAlertDialog(
    title: String,
    width: Dp = 300.dp,
    height: Dp? = null,
    cancelable: Boolean = true,
    dialogType: DialogType = DialogType.Default,
    onDismiss: (DialogController<String>.() -> Unit)? = null,
): DialogController<String> {
    return rememberAlertDialog(
        title = title,
        width = width,
        height = height,
        cancelable = cancelable,
        dialogType = dialogType,
        onDismiss = onDismiss,
        content = { _, message ->
            message?.let {
                Text(message)
            }
        }
    )
}