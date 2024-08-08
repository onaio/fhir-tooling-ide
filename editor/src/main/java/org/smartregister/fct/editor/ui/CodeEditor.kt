package org.smartregister.fct.editor.ui

import androidx.compose.foundation.HorizontalScrollbar
import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.isCtrlPressed
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.smartregister.fct.editor.data.controller.CodeController
import org.smartregister.fct.editor.data.enums.CodeStyle
import org.smartregister.fct.editor.data.transformation.JsonTransformation
import org.smartregister.fct.editor.data.transformation.SMTextTransformation
import org.smartregister.fct.editor.ui.components.Toolbox
import org.smartregister.fct.engine.data.locals.LocalAppSettingViewModel
import org.smartregister.fct.engine.util.uuid


@Composable
fun rememberCodeController(): CodeController {
    val controller by remember { mutableStateOf(CodeController()) }
    return controller
}

@Composable
fun CodeEditor(
    modifier: Modifier = Modifier,
    codeStyle: CodeStyle,
    controller: CodeController,
    key: String = uuid()
) {

    val showToolbox = remember { mutableStateOf(false) }
    val isDarkTheme = LocalAppSettingViewModel.current.appSetting.isDarkTheme
    val textFieldValue = remember(key) { mutableStateOf(TextFieldValue(AnnotatedString(controller.getText()))) }
    val searchText = remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()
    val focusRequester = FocusRequester()

    Box(
        modifier = Modifier.fillMaxSize().then(modifier)
    ) {
        val horizontalScrollState = rememberScrollState()
        val verticalScrollState = rememberScrollState()

        ConstraintLayout(Modifier.fillMaxSize()) {

            val (editorRef, toolboxRef) = createRefs()

            BasicTextField(
                value = textFieldValue.value,
                onValueChange = {
                    textFieldValue.value = it
                    scope.launch(Dispatchers.IO) {
                        controller?.setText(it.text)
                    }
                },
                modifier = Modifier
                    .fillMaxSize()
                    .padding(3.dp)
                    .pointerHoverIcon(PointerIcon.Text)
                    .focusRequester(focusRequester)
                    .onPreviewKeyEvent { keyEvent ->
                        when {
                            keyEvent.isCtrlPressed && keyEvent.key == Key.F && keyEvent.type == KeyEventType.KeyUp -> {
                                showToolbox.value = true
                                true
                            }

                            keyEvent.key == Key.Escape && keyEvent.type == KeyEventType.KeyUp -> {
                                searchText.value = ""
                                showToolbox.value = false
                                scope.launch {
                                    delay(1000)
                                    focusRequester.requestFocus()
                                }
                                true
                            }

                            else -> false
                        }
                    }
                    .verticalScroll(verticalScrollState)
                    .horizontalScroll(horizontalScrollState)
                    .constrainAs(editorRef) {
                        start.linkTo(parent.start)
                        top.linkTo(parent.top)
                        end.linkTo(parent.end)
                        bottom.linkTo(toolboxRef.top)
                        height = Dimension.preferredWrapContent
                    },
                visualTransformation = getTransformation(
                    codeStyle = codeStyle,
                    searchText = searchText.value,
                    isDarkTheme = isDarkTheme,
                    colorScheme = MaterialTheme.colorScheme
                ),
                textStyle = TextStyle(
                    color = MaterialTheme.colorScheme.onBackground,
                    fontFamily = FontFamily.Monospace
                ),
                cursorBrush = SolidColor(MaterialTheme.colorScheme.onBackground)
            )

            Box(
                modifier = Modifier.fillMaxWidth().constrainAs(toolboxRef) {
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom)
                }
            ) {
                if (showToolbox.value) {
                    Toolbox(
                        searchTextInEditor = searchText,
                        showToolbox = showToolbox
                    )
                }
            }

        }

        VerticalScrollbar(
            modifier = Modifier.align(Alignment.CenterEnd).fillMaxHeight(),
            adapter = rememberScrollbarAdapter(
                scrollState = verticalScrollState
            )
        )

        HorizontalScrollbar(
            modifier = Modifier.align(Alignment.BottomCenter).fillMaxWidth(),
            adapter = rememberScrollbarAdapter(
                scrollState = horizontalScrollState
            )
        )
    }
}

private fun getTransformation(
    codeStyle: CodeStyle,
    searchText: String,
    isDarkTheme: Boolean,
    colorScheme: ColorScheme
): VisualTransformation {

    return when (codeStyle) {
        CodeStyle.StructureMap -> SMTextTransformation(searchText, isDarkTheme, colorScheme)
        CodeStyle.Json -> JsonTransformation(searchText, isDarkTheme, colorScheme)
    }
}