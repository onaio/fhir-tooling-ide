package org.smartregister.fct.aurora.ui.components.dialog

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
import org.smartregister.fct.aurora.domain.controller.ConfirmationDialogController

@Composable
fun <T> rememberConfirmationDialog(
    modifier: Modifier = Modifier,
    title: String? = null,
    message: String,
    confirmButtonLabel: String = "Yes",
    cancelButtonLabel: String = "No",
    isCancellable: Boolean = true,
    icon: ImageVector? = null,
    onDismiss: (() -> Unit)? = null,
    onConfirmed: suspend CoroutineScope.(T?) -> Unit
): ConfirmationDialogController<T> {

    val isShowDialog = remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    val confirmationDialogController = remember {
        ConfirmationDialogController<T> {
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
        onDismiss = onDismiss,
        onConfirmed = {
            scope.launch {
                onConfirmed(confirmationDialogController.extra)
            }
        }
    )

    return confirmationDialogController
}

@Composable
fun ConfirmationDialog(
    isShowDialog: MutableState<Boolean>,
    modifier: Modifier = Modifier,
    title: String? = null,
    message: String,
    confirmButtonLabel: String = "Yes",
    cancelButtonLabel: String = "No",
    isCancellable: Boolean = true,
    icon: ImageVector? = null,
    onDismiss: (() -> Unit)?,
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
            onDismissRequest = {
                isShowDialog.value = !isCancellable
                if (isCancellable) onDismiss?.invoke()
            },
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
                            onDismiss?.invoke()
                        }
                    ) {
                        Text(cancelButtonLabel)
                    }
                }
            }
        )
    }
}


@Composable
fun ConfirmationDialog(
    modifier: Modifier = Modifier,
    title: String? = null,
    message: String,
    confirmButtonLabel: String = "Yes",
    cancelButtonLabel: String = "No",
    icon: ImageVector? = null,
    onCancelRequest: () -> Unit = {},
    onDismiss: () -> Unit,
    onConfirmed: () -> Unit
) {

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
        onDismissRequest = onCancelRequest,
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirmed()
                }
            ) {
                Text(confirmButtonLabel)
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss
            ) {
                Text(cancelButtonLabel)
            }
        }
    )
}