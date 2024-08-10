package org.smartregister.fct.radiance.ui.components.dialog

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.smartregister.fct.radiance.domain.model.ConfirmationDialogResult

@Composable
fun <T> rememberConfirmationDialog(
    modifier: Modifier = Modifier,
    title: String? = null,
    message: String,
    confirmButtonLabel: String = "Yes",
    cancelButtonLabel: String = "No",
    isCancellable: Boolean = true,
    icon: ImageVector? = null,
    onConfirmed: suspend CoroutineScope.(T?) -> Unit
): ConfirmationDialogResult<T> {

    val isShowDialog = remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    val confirmationDialogResult = remember {
        ConfirmationDialogResult<T> {
            isShowDialog.value = true
        }
    }

    ConfirmationDialog(
        isShowDialog = isShowDialog,
        modifier = modifier,
        title = title,
        message = message,
        confirmButtonLabel = confirmButtonLabel,
        cancelButtonLabel = cancelButtonLabel,
        isCancellable = isCancellable,
        icon = icon,
        onConfirmed = {
            scope.launch {
                onConfirmed(confirmationDialogResult.extra)
            }
        }
    )

    return confirmationDialogResult
}

@Composable
internal fun ConfirmationDialog(
    isShowDialog: MutableState<Boolean>,
    modifier: Modifier = Modifier,
    title: String? = null,
    message: String,
    confirmButtonLabel: String = "Yes",
    cancelButtonLabel: String = "No",
    isCancellable: Boolean = true,
    icon: ImageVector? = null,
    onConfirmed: () -> Unit
) {

    if (isShowDialog.value) {
        AlertDialog(
            modifier = modifier,
            icon = {
                icon?.run {
                    Icon(this, contentDescription = null)
                }

            },
            title = {
                title?.run {
                    Text(this)
                }

            },
            text = {
                Text(message)
            },
            onDismissRequest = { isShowDialog.value = !isCancellable },
            confirmButton = {
                TextButton(
                    onClick = {
                        isShowDialog.value = false
                        onConfirmed()
                    }
                ) {
                    Text(confirmButtonLabel)
                }
            },
            dismissButton = {
                if (isCancellable) {
                    TextButton(
                        onClick = {
                            isShowDialog.value = false
                        }
                    ) {
                        Text(cancelButtonLabel)
                    }
                }
            }
        )
    }
}