package org.smartregister.fct.text_viewer.ui.dialog

import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.ComponentContext
import org.smartregister.fct.common.data.controller.DialogController
import org.smartregister.fct.common.presentation.ui.dialog.rememberDialog
import org.smartregister.fct.text_viewer.ui.view.TextViewer

@Composable
fun rememberTextViewerDialog(
    componentContext: ComponentContext,
    title: String = "Text Viewer / Formatter",
    cancelable: Boolean = true,
    onDismiss: (DialogController<String>.() -> Unit)? = null,
): DialogController<String> {

    val controller = rememberDialog(
        title = title,
        width = 1000.dp,
        height = 800.dp,
        cancelable = cancelable,
        cancelOnTouchOutside = true,
        onDismiss = onDismiss
    ) { _, text ->

        TextViewer(
            componentContext = componentContext,
            text = text ?: ""
        )
    }

    return controller
}