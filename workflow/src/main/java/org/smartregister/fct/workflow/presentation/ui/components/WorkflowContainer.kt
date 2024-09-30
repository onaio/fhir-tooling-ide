package org.smartregister.fct.workflow.presentation.ui.components

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import kotlinx.coroutines.flow.collectLatest
import org.smartregister.fct.common.data.controller.SingleFieldDialogController
import org.smartregister.fct.common.domain.model.ResizeOption
import org.smartregister.fct.common.presentation.ui.components.HorizontalSplitPane
import org.smartregister.fct.workflow.presentation.components.BaseWorkflowComponent

@Composable
internal fun WorkflowContainer(
    component: BaseWorkflowComponent,
    newWorkflowDialog: SingleFieldDialogController
) {

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Toolbar(component, newWorkflowDialog)

        Row(Modifier.fillMaxSize()) {

            WorkflowFiles(component)
            WorkflowEditor(component)
            /*HorizontalSplitPane(
                resizeOption = ResizeOption.Flexible(
                    minSizeRatio = 0.1f,
                    maxSizeRatio = 0.9f,
                    sizeRatio = 0.2f,
                ),
                leftContent = {},
                rightContent = {
                    WorkflowEditor(component)
                },
            )*/
        }

    }
}

