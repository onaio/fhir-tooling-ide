package org.smartregister.fct.presentation.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.runtime.Composable
import com.arkivanov.decompose.ComponentContext
import org.smartregister.fct.aurora.presentation.ui.components.SmallIconButton
import org.smartregister.fct.aurora.presentation.ui.components.TooltipPosition
import org.smartregister.fct.text_viewer.ui.dialog.rememberTextViewerDialog

@Composable
internal fun TextViewerButton(
    componentContext: ComponentContext
) {

    val textViewerDialog = rememberTextViewerDialog(
        componentContext = componentContext
    )

    SmallIconButton(
        tooltip = "Text Viewer / Formatter",
        tooltipPosition = TooltipPosition.Bottom(),
        onClick = {
            textViewerDialog.show()
        },
        icon = Icons.Outlined.Edit,
        alpha = 0.7f
    )
}