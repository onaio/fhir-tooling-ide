package org.smartregister.fct.workflow.presentation.components

import com.arkivanov.decompose.ComponentContext
import org.koin.core.component.KoinComponent
import org.smartregister.fct.common.presentation.component.ScreenComponent
import org.smartregister.fct.workflow.domain.model.Workflow

internal class ApplyWorkflowComponent(
    workflow: Workflow,
    screenComponent: WorkflowScreenComponent) : BaseWorkflowComponent(workflow, screenComponent) {

}