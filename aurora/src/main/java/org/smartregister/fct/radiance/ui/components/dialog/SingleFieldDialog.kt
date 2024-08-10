package org.smartregister.fct.radiance.ui.components.dialog

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material3.AlertDialog
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
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.smartregister.fct.radiance.domain.model.SingleFieldDialogResult
import java.util.UUID

@Composable
fun rememberSingleFieldDialog(
    modifier: Modifier = Modifier,
    title: String? = null,
    okButtonLabel: String = "OK",
    cancelButtonLabel: String = "Cancel",
    isCancellable: Boolean = true,
    placeholder: String = "",
    maxLength: Int = 40,
    key: Any? = UUID.randomUUID().toString(),
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
    key: Any? = UUID.randomUUID().toString(),
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