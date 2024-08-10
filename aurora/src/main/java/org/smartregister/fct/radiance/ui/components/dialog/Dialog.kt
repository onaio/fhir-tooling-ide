package org.smartregister.fct.radiance.ui.components.dialog

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import org.smartregister.fct.radiance.domain.model.DialogController
import org.smartregister.fct.radiance.ui.components.SmallIconButton
import androidx.compose.ui.window.Dialog as MatDialog

@Composable
fun rememberDialogController(
    title: String,
    width: Dp = 300.dp,
    height: Dp? = null,
    cancelable: Boolean = true,
    content: @Composable (ColumnScope.(DialogController) -> Unit)
): DialogController {

    val isShowDialog = remember { mutableStateOf(false) }

    val dialogController = remember {
        DialogController(
            onShow = { isShowDialog.value = true },
            onHide = { isShowDialog.value = false }
        )
    }

    Dialog(
        dialogController = dialogController,
        isShowDialog = isShowDialog,
        title = title,
        width = width,
        height = height,
        cancelable = cancelable,
        content = content
    )

    return dialogController
}

@Composable
internal fun Dialog(
    dialogController: DialogController,
    isShowDialog: MutableState<Boolean>,
    title: String,
    width: Dp,
    height: Dp?,
    cancelable: Boolean,
    content: @Composable (ColumnScope.(DialogController) -> Unit)
) {


    if (isShowDialog.value) {
        MatDialog(
            properties = DialogProperties(
                usePlatformDefaultWidth = false
            ),
            onDismissRequest = { isShowDialog.value = !cancelable }
        ) {

            var rootModifier = Modifier.width(width)
            if (height != null) rootModifier = rootModifier.height(height)

            Card(
                modifier = rootModifier
            ) {
                Box(
                    modifier = Modifier.background(MaterialTheme.colorScheme.surfaceContainer)
                        .padding(horizontal = 14.dp, vertical = 10.dp)
                        .fillMaxWidth(),
                ) {
                    Text(
                        modifier = Modifier.align(Alignment.Center),
                        text = title,
                        style = MaterialTheme.typography.titleMedium
                    )

                    if (cancelable) {
                        SmallIconButton(
                            modifier = Modifier.width(18.dp).align(Alignment.CenterEnd),
                            icon = Icons.Outlined.Close,
                            onClick = { isShowDialog.value = false }
                        )
                    }
                }
                HorizontalDivider()

                var bodyModifier = Modifier.background(MaterialTheme.colorScheme.background).fillMaxWidth()
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