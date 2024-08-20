package org.smartregister.fct.serverconfig.presentation.components

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
    serverConfigPanelComponent: ServerConfigPanelComponent
) : KoinComponent, ConfigDialogComponent(serverConfigPanelComponent) {

    private val appSettingManager: AppSettingManager by inject()
    private var appSetting = appSettingManager.appSetting

    init {
        componentScope.launch {
            appSettingManager.getAppSettingFlow().collectLatest {
                appSetting = it
            }
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