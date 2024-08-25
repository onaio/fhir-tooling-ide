package org.smartregister.fct.fhirman.domain.model

import org.smartregister.fct.editor.data.controller.CodeController
import org.smartregister.fct.engine.domain.model.HttpMethodType

internal data class ServerTabContent(
    val title: String,
    var methodType: HttpMethodType = HttpMethodType.Get,
    var resourceType: String,
    var resourceId: String,
    var bodyController: CodeController,
    var responseController: CodeController
)
