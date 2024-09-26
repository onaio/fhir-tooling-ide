package org.smartregister.fct.text_viewer.ui.components

import androidx.compose.foundation.HorizontalScrollbar
import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.isAltPressed
import androidx.compose.ui.input.key.isCtrlPressed
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import org.smartregister.fct.common.data.manager.AuroraManager
import org.smartregister.fct.text_viewer.util.compactText
import org.smartregister.fct.text_viewer.util.formatText

context (AuroraManager)
@Composable
internal fun Editor(
    textState: MutableState<String>,
    tabIndentState: MutableState<Int>,
) {

    var lineNumbers by remember { mutableStateOf("") }
    var lineNumbersTopPadding by remember { mutableStateOf(18.dp) }
    val horizontalScrollState = rememberScrollState()
    val verticalScrollState = rememberScrollState()

    LaunchedEffect(textState.value) {
        lineNumbers = getLineNumbers(textState.value)
    }

    lineNumbersTopPadding = if (lineNumbers.contains("2")) {
        16.dp
    } else {
        19.dp
    }

    var textEditorWidth by remember { mutableStateOf(1000.dp) }
    var lineNumberWidth by remember { mutableStateOf(10.dp) }

    Box(
        Modifier.fillMaxSize().onGloballyPositioned {
            textEditorWidth = it.size.width.dp
        }
    ) {
        Row {
            Box(
                modifier = Modifier.widthIn(min = 30.dp).fillMaxHeight()
                    .background(MaterialTheme.colorScheme.surfaceContainer.copy(alpha = 0.6f))
                    .padding(start = 5.dp, top = lineNumbersTopPadding, end = 5.dp, bottom = 4.dp)
                    .onGloballyPositioned {
                        lineNumberWidth = it.size.width.dp + 10.dp
                    }
            ) {
                Text(
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .verticalScroll(verticalScrollState),
                    text = lineNumbers,
                    style = TextStyle(
                        fontFamily = FontFamily.Monospace,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
                    ),
                )
            }

            Box {

                TextField(
                    modifier = Modifier
                        .width(textEditorWidth - lineNumberWidth)
                        .fillMaxHeight()
                        .verticalScroll(verticalScrollState)
                        .onPreviewKeyEvent { keyEvent ->
                            when {
                                keyEvent.isCtrlPressed && keyEvent.isAltPressed && keyEvent.key == Key.K && keyEvent.type == KeyEventType.KeyUp -> {
                                    textState.compactText {
                                        showErrorSnackbar(it)
                                    }
                                    true
                                }

                                keyEvent.isCtrlPressed && keyEvent.isAltPressed && keyEvent.key == Key.L && keyEvent.type == KeyEventType.KeyUp -> {
                                    textState.formatText(tabIndentState.value) {
                                        showErrorSnackbar(it)
                                    }
                                    true
                                }

                                else -> false
                            }
                        },
                    value = textState.value,
                    onValueChange = {
                        lineNumbers = getLineNumbers(it)
                        textState.value = it
                    },

                    textStyle = TextStyle(
                        fontFamily = FontFamily.Monospace,
                        textAlign = TextAlign.Start,
                    ),
                    singleLine = false,
                    colors = TextFieldDefaults.colors(
                        unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainer.copy(
                            alpha = 0.3f
                        ),
                        focusedContainerColor = MaterialTheme.colorScheme.surfaceContainer.copy(
                            alpha = 0.3f
                        ),
                        unfocusedIndicatorColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        cursorColor = MaterialTheme.colorScheme.onSurface,
                    ),
                )

                VerticalScrollbar(
                    modifier = Modifier.align(Alignment.CenterEnd).fillMaxHeight(),
                    adapter = rememberScrollbarAdapter(
                        scrollState = verticalScrollState
                    )
                )
            }
        }
    }
}

private fun getLineNumbers(text: String): String {
    return List(
        "\n".toRegex().findAll(text).toList().size + 1
    ) { index ->
        "${index + 1}"
    }.joinToString("\n")
}