package org.smartregister.fct.fhirman.data.manager

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.smartregister.fct.common.data.controller.TabsControllerImpl
import org.smartregister.fct.common.domain.model.TabType
import org.smartregister.fct.editor.data.controller.CodeController
import org.smartregister.fct.editor.data.enums.FileType
import org.smartregister.fct.engine.domain.model.HttpMethodType
import org.smartregister.fct.engine.util.componentScope
import org.smartregister.fct.fhirman.domain.model.ServerTabContent
import org.smartregister.fct.fhirman.presentation.components.FhirmanServerComponent
import org.smartregister.fct.fhirman.presentation.components.FhirmanServerTabComponent

internal class FhirmanServerTabsManager() {

    private val scope = CoroutineScope(Dispatchers.IO)
    val items = mutableListOf<FhirmanServerTabComponent>()

    val options = listOf (
        HttpMethodType.Get,
        HttpMethodType.Post,
        HttpMethodType.Put,
        HttpMethodType.Delete,
    )

    val controller = TabsControllerImpl(
        items = items,
        title = { it.content.title },
        tabType = TabType.Scrollable
    )

    fun addNewTab(component: FhirmanServerComponent, title: String) {
        component.componentScope.launch {
            val item = ServerTabContent(
                title = title,
                resourceType = "",
                resourceId = "",
                bodyController = CodeController(scope, fileType = FileType.Json),
                responseController = CodeController(scope, fileType = FileType.Json, readOnly = true)
            )

            val tabComponent = FhirmanServerTabComponent(
                parentComponent = component,
                content = item
            )

            controller.add(tabComponent)
        }
    }
}