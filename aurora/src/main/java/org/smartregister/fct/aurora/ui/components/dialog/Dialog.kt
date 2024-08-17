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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
fun rememberDialog(
    title: String,
    width: Dp = 300.dp,
    height: Dp? = null,
    cancelable: Boolean = true,
    onCancelled: (() -> Unit)? = null,
    dialogType: DialogType = DialogType.Default,
    content: @Composable (ColumnScope.(DialogController) -> Unit)
): DialogController {

    val isShowDialog = remember { mutableStateOf(false) }

    val dialogController = remember {
        DialogController(
            onShow = {
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
        height = height,
        cancelable = cancelable,
        onCancelled = onCancelled,
        content = content
    )

    return dialogController
}

@Composable
internal fun Dialog(
    dialogController: DialogController,
    isShowDialog: MutableState<Boolean>,
    dialogType: DialogType,
    title: String,
    width: Dp,
    height: Dp?,
    cancelable: Boolean,
    onCancelled: (() -> Unit)? = null,
    content: @Composable (ColumnScope.(DialogController) -> Unit)
) {

    if (isShowDialog.value) {

        val borderColor =
            if (dialogType == DialogType.Error) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.surface
        val titleBackground =
            if (dialogType == DialogType.Error) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.surfaceContainer
        val titleForeground =
            if (dialogType == DialogType.Error) MaterialTheme.colorScheme.onError else MaterialTheme.colorScheme.onSecondaryContainer

        MatDialog(
            properties = DialogProperties(
                usePlatformDefaultWidth = false
            ),
            onDismissRequest = {
                isShowDialog.value = !cancelable
                if (cancelable) onCancelled?.invoke()
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
                                onCancelled?.invoke()
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
                        content(dialogController)
                    }
                )
            }
        }
    }
}