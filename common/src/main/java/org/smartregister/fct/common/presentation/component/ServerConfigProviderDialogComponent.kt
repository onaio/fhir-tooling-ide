package org.smartregister.fct.common.presentation.component

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.hl7.fhir.r4.model.Resource
import org.hl7.fhir.r4.model.ResourceType
import org.json.JSONObject
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.smartregister.fct.apiclient.domain.model.AuthRequest
import org.smartregister.fct.apiclient.domain.model.AuthResponse
import org.smartregister.fct.apiclient.domain.model.UploadResourceRequest
import org.smartregister.fct.apiclient.domain.model.UploadResponse
import org.smartregister.fct.apiclient.domain.usecase.AuthenticateClient
import org.smartregister.fct.apiclient.domain.usecase.UploadResource
import org.smartregister.fct.engine.data.manager.AppSettingManager
import org.smartregister.fct.engine.domain.model.ServerConfig
import org.smartregister.fct.engine.util.componentScope
import org.smartregister.fct.engine.util.decodeResourceFromString
import org.smartregister.fct.engine.util.logicalId
import org.smartregister.fct.logger.FCTLogger

internal class ServerConfigProviderDialogComponent(
    componentContext: ComponentContext,
    private val prevSelectedConfig: ServerConfig?
) : KoinComponent, ComponentContext by componentContext {

    private val appSettingManager: AppSettingManager by inject()
    private val _configs = MutableValue<List<ServerConfig>>(listOf())
    val configs: Value<List<ServerConfig>> = _configs

    private val _selectedConfig = MutableStateFlow(prevSelectedConfig)
    val selectedConfig: StateFlow<ServerConfig?> = _selectedConfig

    init {
        loadConfigs()
    }

    private fun loadConfigs() {
        componentScope.launch {
            appSettingManager.appSetting.getServerConfigsAsFlow().collectLatest {
                _configs.value = it
            }
        }
    }

    fun selectConfig(config: ServerConfig) {
        componentScope.launch {
            _selectedConfig.emit(config)
        }
    }
}