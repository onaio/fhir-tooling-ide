package org.smartregister.fct.fhirman.presentation.components

import com.arkivanov.decompose.ComponentContext
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.smartregister.fct.apiclient.domain.model.Request
import org.smartregister.fct.apiclient.domain.model.Response
import org.smartregister.fct.apiclient.domain.usecase.ApiRequest
import org.smartregister.fct.common.data.manager.AuroraManager
import org.smartregister.fct.common.domain.model.Message
import org.smartregister.fct.engine.domain.model.HttpMethodType
import org.smartregister.fct.engine.util.componentScope
import org.smartregister.fct.fhirman.domain.model.ServerTabContent

internal class FhirmanServerTabComponent(
    private val parentComponent: FhirmanServerComponent,
    val content: ServerTabContent,
    private val auroraManager: AuroraManager = parentComponent.auroraManager
) : KoinComponent, ComponentContext by parentComponent {

    private val apiRequest: ApiRequest by inject()

    fun send() {
        componentScope.launch {
            buildRequest()?.let {
                auroraManager.showLoader()
                when (val response = apiRequest(it)) {
                    is Response.Success -> {
                        auroraManager.hideLoader()
                        if (content.methodType is HttpMethodType.Delete) {
                            auroraManager.showSnackbar("${content.resourceType}/${content.resourceId} successfully deleted")
                        }

                        content.responseController.setPostText(response.response)
                    }

                    is Response.Failed -> {
                        auroraManager.hideLoader()
                        auroraManager.showSnackbar(
                            Message.Error(
                                response.outcome.issue.firstOrNull()?.diagnostics ?: "Unknown Error"
                            )
                        )
                    }
                }

            }
        }
    }

    private fun buildRequest(): Request? {
        val selectedConfig = parentComponent.selectedConfig.value

        if (selectedConfig == null) {
            auroraManager.showSnackbar(Message.Error("Server config is not selected."))
            return null
        }

        if (content.resourceType.trim().isEmpty()) {
            auroraManager.showSnackbar(Message.Error("Resource type could not be empty."))
            return null
        }

        if (content.resourceId.trim().isEmpty()) {
            auroraManager.showSnackbar(Message.Error("Resource id could not be empty."))
            return null
        }

        return Request(
            config = selectedConfig,
            methodType = content.methodType,
            resourceType = content.resourceType,
            resourceId = content.resourceId,
            body = content.bodyController.getText()
        )
    }
}