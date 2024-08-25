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
import org.smartregister.fct.common.data.manager.AuroraManager
import org.smartregister.fct.engine.data.manager.AppSettingManager
import org.smartregister.fct.engine.domain.model.ServerConfig
import org.smartregister.fct.engine.util.componentScope

internal class FhirmanServerComponent(
    componentContext: ComponentContext,
    val auroraManager: AuroraManager
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
                if (_selectedConfig.value != null && _selectedConfig.value !in it.serverConfigs) {
                    _selectedConfig.emit(null)
                }
            }
        }
    }

    fun selectConfig(config: ServerConfig) {
        componentScope.launch {
            _selectedConfig.emit(config)
        }
    }
}