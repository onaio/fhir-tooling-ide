package org.smartregister.fct.sm.presentation.component

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.instancekeeper.getOrCreate
import org.hl7.fhir.r4.model.Resource
import org.smartregister.fct.editor.data.controller.CodeController
import org.smartregister.fct.editor.data.enums.FileType
import org.smartregister.fct.engine.util.componentScope
import org.smartregister.fct.engine.util.encodeResourceToString
import org.smartregister.fct.engine.util.logicalId

internal class StructureMapResultTabComponent(
    componentContext: ComponentContext,
    override val resource: Resource
) :
    ResultTabComponent, ComponentContext by componentContext {

    override val codeController =
        instanceKeeper.getOrCreate("code-controller-${resource.logicalId}-${resource.hashCode()}") {
            CodeController(
                scope = componentScope,
                initialText = resource.encodeResourceToString(),
                fileType = FileType.Json
            )
        }
}