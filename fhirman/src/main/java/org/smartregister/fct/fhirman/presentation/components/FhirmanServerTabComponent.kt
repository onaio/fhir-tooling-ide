package org.smartregister.fct.fhirman.presentation.components

import com.arkivanov.decompose.ComponentContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.smartregister.fct.apiclient.domain.model.Request
import org.smartregister.fct.apiclient.domain.model.Response
import org.smartregister.fct.apiclient.domain.usecase.ApiRequest
import org.smartregister.fct.common.data.manager.AuroraManager
import org.smartregister.fct.common.domain.model.Message
import org.smartregister.fct.engine.data.manager.AppSettingManager
import org.smartregister.fct.engine.domain.model.HttpMethodType
import org.smartregister.fct.engine.domain.model.ServerConfig
import org.smartregister.fct.engine.util.componentScope
import org.smartregister.fct.fhirman.domain.model.ServerTabContent

internal class FhirmanServerTabComponent(
    private val parentComponent: FhirmanServerComponent,
    val content: ServerTabContent,
) : KoinComponent, ComponentContext by parentComponent {

    private val appSettingManager: AppSettingManager by inject()
    private val apiRequest: ApiRequest by inject()

    private val _selectedConfig = MutableStateFlow<ServerConfig?>(null)
    val selectedConfig: StateFlow<ServerConfig?> = _selectedConfig

    private var scope = componentScope
    private var auroraManager: AuroraManager = parentComponent.auroraManager

    init {
        listenConfigs()
    }

    fun updateScope(scope: CoroutineScope, auroraManager: AuroraManager) {
        this.scope = scope
        this.auroraManager = auroraManager
    }

    private fun listenConfigs() {
        scope.launch {
            appSettingManager.appSetting.getServerConfigsAsFlow().collectLatest {
                if (_selectedConfig.value != null && _selectedConfig.value !in it) {
                    _selectedConfig.emit(null)
                }
            }
        }
    }

    fun selectConfig(config: ServerConfig) {
        scope.launch {
            _selectedConfig.emit(config)
        }
    }

    fun send() {
        scope.launch {
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
        if (_selectedConfig.value == null) {
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
            config = _selectedConfig.value!!,
            methodType = content.methodType,
            resourceType = content.resourceType,
            resourceId = content.resourceId,
            body = content.bodyController.getText()
        )
    }
}