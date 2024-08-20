package org.smartregister.fct.serverconfig.presentation.components

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.smartregister.fct.common.data.manager.AppSettingManager
import org.smartregister.fct.common.domain.model.ServerConfig
import org.smartregister.fct.common.util.componentScope
import org.smartregister.fct.common.util.decodeJson
import org.smartregister.fct.common.util.uuid
import org.smartregister.fct.logger.FCTLogger
import org.smartregister.fct.serverconfig.domain.model.ImportDialogState

class ImportConfigDialogComponent(
    private val serverConfigPanelComponent: ServerConfigPanelComponent
) : KoinComponent, ComponentContext by serverConfigPanelComponent {

    private val appSettingManager: AppSettingManager by inject()
    private var appSetting = appSettingManager.appSetting

    private val _checkedConfigs = MutableValue<List<ServerConfig>>(listOf())
    val checkedConfigs: Value<List<ServerConfig>> = _checkedConfigs

    init {
        componentScope.launch {
            appSettingManager.getAppSettingFlow().collectLatest {
                appSetting = it
            }
        }
    }

    fun addOrRemoveConfig(checked: Boolean, config: ServerConfig) {
        if (checked) {
            _checkedConfigs.value += listOf(config)
        } else {
            _checkedConfigs.value = _checkedConfigs
                .value
                .filter { it.id != config.id }
        }
    }

    fun loadConfigs(configJson: String) {
        componentScope.launch {
            try {
                serverConfigPanelComponent.importDialogState.value =
                    ImportDialogState.SelectConfigsDialog(
                        component = this@ImportConfigDialogComponent,
                        configs = configJson.decodeJson<List<ServerConfig>>()
                    )
            } catch (ex: Exception) {
                FCTLogger.e(ex)
                serverConfigPanelComponent.importDialogState.value =
                    ImportDialogState.ImportErrorDialog(
                        error = ex.message ?: "JSON Parse error"
                    )
            }
        }
    }

    fun import() {
        componentScope.launch {

            val mergedConfigs: List<ServerConfig> =
                appSetting.serverConfigs + checkedConfigs.value.map {
                    it.copy(
                        id = uuid()
                    )
                }

            val updatedAppSetting = appSetting.copy(
                serverConfigs = mergedConfigs
            )

            appSettingManager.setAndUpdate(updatedAppSetting)
            serverConfigPanelComponent.hideImportConfigDialog()
        }
    }
}