package org.smartregister.fct.aurora.ui.components.dialog


import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import org.smartregister.fct.aurora.domain.controller.DialogController
import org.smartregister.fct.aurora.ui.components.SmallIconButton
import androidx.compose.ui.window.Dialog as MatDialog

enum class DialogType {
    Default, Error
}

@Composable
fun <T> rememberDialog(
    title: String,
    width: Dp = 300.dp,
    height: Dp? = null,
    cancelable: Boolean = true,
    cancelOnTouchOutside: Boolean = true,
    dialogType: DialogType = DialogType.Default,
    onDismiss: (DialogController<T>.() -> Unit)? = null,
    content: @Composable (ColumnScope.(DialogController<T>, T?) -> Unit)
): DialogController<T> {

    val isShowDialog = remember { mutableStateOf(false) }
    var data by remember { mutableStateOf<T?>(null) }

    val dialogController = remember {
        DialogController<T>(
            onShow = {
                data = it
                isShowDialog.value = true
            },
            onHide = {
                isShowDialog.value = false
                onDismiss?.invoke(this)
            }
        )
    }

    Dialog(
        dialogController = dialogController,
        isShowDialog = isShowDialog,
        dialogType = dialogType,
        title = title,
        width = width,
        height = height,
        cancelable = cancelable,
        cancelOnTouchOutside = cancelOnTouchOutside,
        onDismiss = onDismiss,
        data = data,
        content = content
    )

    return dialogController
}

@Composable
internal fun <T> Dialog(
    dialogController: DialogController<T>,
    isShowDialog: MutableState<Boolean>,
    dialogType: DialogType,
    title: String,
    width: Dp,
    height: Dp?,
    cancelable: Boolean,
    cancelOnTouchOutside: Boolean,
    onDismiss: (DialogController<T>.() -> Unit)?,
    data: T?,
    content: @Composable (ColumnScope.(DialogController<T>, T?) -> Unit)
) {

    if (isShowDialog.value) {

        val borderColor =
            if (dialogType == DialogType.Error) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.outline
        val titleBackground =
            if (dialogType == DialogType.Error) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.surface
        val titleForeground =
            if (dialogType == DialogType.Error) MaterialTheme.colorScheme.onError else MaterialTheme.colorScheme.onSecondaryContainer

        MatDialog(
            properties = DialogProperties(
                usePlatformDefaultWidth = false
            ),
            onDismissRequest = {
                isShowDialog.value = !cancelOnTouchOutside
                if (cancelOnTouchOutside) onDismiss?.invoke(dialogController)
            }
        ) {

            var rootModifier = Modifier.width(width)
            if (height != null) rootModifier = rootModifier.height(height)

            Card(
                modifier = rootModifier,
                shape = RoundedCornerShape(6.dp),
                border = BorderStroke(
                    width = 1.dp,
                    color = borderColor
                ),
                colors = CardDefaults.cardColors(
                    containerColor = Color.Transparent
                )
            ) {
                Box(
                    modifier = Modifier.background(titleBackground)
                        .padding(horizontal = 16.dp, vertical = 12.dp)
                        .fillMaxWidth(),
                ) {
                    Text(
                        modifier = Modifier.align(Alignment.Center),
                        text = title,
                        color = titleForeground,
                        style = MaterialTheme.typography.titleMedium
                    )

                    if (cancelable) {
                        SmallIconButton(
                            modifier = Modifier.width(18.dp).align(Alignment.CenterEnd),
                            icon = Icons.Outlined.Close,
                            tint = titleForeground,
                            onClick = {
                                isShowDialog.value = false
                                onDismiss?.invoke(dialogController)
                            }
                        )
                    }
                }
                HorizontalDivider()

                var bodyModifier =
                    Modifier.background(MaterialTheme.colorScheme.background).fillMaxWidth()
                if (height != null) bodyModifier = bodyModifier.height(height)

                Column(
                    modifier = bodyModifier,
                    content = {
                        content(dialogController, data)
                    }
                )
            }
        }
    }
}