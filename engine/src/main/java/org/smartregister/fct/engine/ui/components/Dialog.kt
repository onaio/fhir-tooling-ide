package org.smartregister.fct.engine.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.smartregister.fct.engine.domain.model.ConfirmationDialogResult
import org.smartregister.fct.engine.domain.model.SingleFieldDialogResult
import org.smartregister.fct.engine.util.uuid


@Composable
fun rememberSingleFieldDialog(
    modifier: Modifier = Modifier,
    title: String? = null,
    okButtonLabel: String = "OK",
    cancelButtonLabel: String = "Cancel",
    isCancellable: Boolean = true,
    placeholder: String = "",
    maxLength: Int = 40,
    key: Any? = uuid(),
    onResult: suspend CoroutineScope.(String) -> Unit
): SingleFieldDialogResult {

    val isShowDialog = remember { mutableStateOf(false) }

    val singleFieldDialogResult = remember {
        SingleFieldDialogResult {
            isShowDialog.value = true
        }
    }

    SingleFieldDialog(
        isShowDialog = isShowDialog,
        modifier = modifier,
        title = title,
        okButtonLabel = okButtonLabel,
        cancelButtonLabel = cancelButtonLabel,
        isCancellable = isCancellable,
        placeholder = placeholder,
        maxLength = maxLength,
        key = key,
        onResult = onResult
    )

    return singleFieldDialogResult
}

@Composable
internal fun SingleFieldDialog(
    isShowDialog: MutableState<Boolean>,
    modifier: Modifier = Modifier,
    title: String? = null,
    okButtonLabel: String = "OK",
    cancelButtonLabel: String = "Cancel",
    isCancellable: Boolean = true,
    placeholder: String = "",
    maxLength: Int = 40,
    key: Any? = uuid(),
    onResult: suspend CoroutineScope.(String) -> Unit
) {

    var isError by remember(key) { mutableStateOf(false) }
    var value by remember(key) { mutableStateOf("") }
    val focusResult = remember { FocusRequester() }
    val scope = rememberCoroutineScope()

    if (isShowDialog.value) {
        AlertDialog(
            modifier = modifier.width(400.dp),
            title = {
                title?.run {
                    Text(this)
                }
            },
            text = {
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth().focusRequester(focusResult),
                    value = value,
                    onValueChange = {
                        if (it.length <= maxLength) value = it
                        isError = value.isEmpty()
                    },
                    isError = isError,
                    singleLine = true,
                    placeholder = {
                        Text(placeholder)
                    }
                )
            },
            onDismissRequest = {
                isShowDialog.value = !isCancellable
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        if (value.trim().isEmpty()) {
                            isError = true
                        } else {
                            isShowDialog.value = false
                            scope.launch {
                                onResult(value)
                            }
                        }
                    }
                ) {
                    Text(okButtonLabel)
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
        scope.launch {
            focusResult.requestFocus()
        }
    }
}

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