package org.smartregister.fct.fhirman.presentation.components

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.smartregister.fct.common.data.manager.AppSettingManager
import org.smartregister.fct.common.domain.model.ServerConfig
import org.smartregister.fct.common.util.componentScope

class FhirmanServerComponent(
    componentContext: ComponentContext
) : KoinComponent, ComponentContext by componentContext {

    private val appSettingManager: AppSettingManager by inject()

    private val _configs = MutableValue(listOf<ServerConfig>())
    val configs: Value<List<ServerConfig>> = _configs

    private val _selectedConfig = MutableStateFlow<ServerConfig?>(null)
    val selectedConfig: StateFlow<ServerConfig?> = _selectedConfig

    init {
        listenConfigs()
    }

    private fun listenConfigs() {
        componentScope.launch {
            appSettingManager.getAppSettingFlow().collectLatest {

                _configs.value = it.serverConfigs
            }
        }
    }

    fun selectConfig(config: ServerConfig) {
        componentScope.launch {
            _selectedConfig.emit(config)
        }
    }
}