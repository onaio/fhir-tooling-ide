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
    formatOnStart: Boolean = false,
    cancelable: Boolean = true,
    callback: ((String) -> Unit)? = null,
    onDismiss: (DialogController<String>.() -> Unit)? = null,
): DialogController<String> {

    val controller = rememberDialog(
        title = title,
        width = 1000.dp,
        height = 800.dp,
        cancelable = cancelable,
        cancelOnTouchOutside = true,
        onDismiss = onDismiss
    ) { c, text ->

        val cb : ((String) -> Unit)? = if (callback != null) { data ->
            c.hide()
            callback(data)
        } else null

        TextViewer(
            componentContext = componentContext,
            formatOnStart = formatOnStart,
            callback = cb,
            text = text ?: ""
        )
    }

    return controller
}