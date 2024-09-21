package org.smartregister.fct.datatable.presentation.ui.components

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.smartregister.fct.datatable.data.controller.DataTableController
import org.smartregister.fct.datatable.domain.feature.DTFilterColumn
import org.smartregister.fct.datatable.domain.model.DataFilterColumn
import org.smartregister.fct.datatable.domain.model.DataFilterTypeColumn

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun DataFilterTextField(
    modifier: Modifier = Modifier,
    controller: DataTableController,
    dtColumn: DTFilterColumn
) {

    val scope = rememberCoroutineScope()
    val tempValue = controller.tempFilterValue[dtColumn.index] ?: ""
    val filterText = dtColumn.value

    filterText.useDebounce {
        scope.launch {
            if (dtColumn.value != tempValue) {
                controller.tempFilterValue[dtColumn.index] = dtColumn.value
                controller.filter()
            }
        }
    }

    BasicTextField(
        modifier = modifier.fillMaxSize(),
        value = filterText,
        onValueChange = { text ->
            if (text.length > 40) return@BasicTextField
            scope.launch {
                controller.filterColumns[dtColumn.index]?.emit(
                    dtColumn
                        .takeIf { it is DataFilterTypeColumn }
                        ?.let { it as DataFilterTypeColumn }
                        ?.copy(value = text)
                        ?: dtColumn
                            .let { it as DataFilterColumn }
                            .copy(value = text)
                )
            }
        },
        textStyle = TextStyle(
            fontSize = MaterialTheme.typography.bodySmall.fontSize,
            color = MaterialTheme.colorScheme.onSurface
        ),
        singleLine = true,
        decorationBox = @Composable { innerTextField ->
            // places leading icon, text field with label and placeholder, trailing icon
            TextFieldDefaults.DecorationBox(
                value = filterText ?: "",
                visualTransformation = VisualTransformation.None,
                innerTextField = innerTextField,
                placeholder = {
                    Text(
                        text = dtColumn.name,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(0.5f)
                    )
                },
                label = null,
                leadingIcon = null,
                trailingIcon = null,
                prefix = null,
                suffix = null,
                supportingText = null,
                shape = TextFieldDefaults.shape,
                singleLine = true,
                enabled = true,
                isError = false,
                interactionSource = remember { MutableInteractionSource() },
                contentPadding = PaddingValues(8.dp),
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainer.copy(alpha = 0.2f),
                    focusedContainerColor = MaterialTheme.colorScheme.surfaceContainer,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedTextColor = MaterialTheme.colorScheme.onSurface,
                    unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                ),
            )
        }
    )
}

@Composable
private fun <T> T.useDebounce(
    delayMillis: Long = 300L,
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    onChange: (T) -> Unit
): T {
    // 2. updating state
    val state by rememberUpdatedState(this)

    // 3. launching the side-effect handler
    DisposableEffect(state) {
        val job = coroutineScope.launch {
            delay(delayMillis)
            onChange(state)
        }
        onDispose {
            job.cancel()
        }
    }
    return state
}