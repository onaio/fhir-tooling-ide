package org.smartregister.fct.sm.presentation.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import org.smartregister.fct.aurora.ui.components.ScreenContainer
import org.smartregister.fct.editor.ui.CodeEditor
import org.smartregister.fct.sm.presentation.component.TabComponent

@Composable
internal fun StructureMapTabItem(component: TabComponent) {

    ScreenContainer(
        panelWidth = 230.dp,
        leftPanel = { StructureMapControlPanel(component) }
    ) {
        CodeEditor(
            controller = component.codeController
        )
    }
}