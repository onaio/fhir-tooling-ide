package org.smartregister.fct.engine.data.manager

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.smartregister.fct.engine.domain.model.AppSetting
import org.smartregister.fct.engine.domain.model.ServerConfig
import org.smartregister.fct.engine.domain.usecase.GetAppSetting
import org.smartregister.fct.engine.domain.usecase.UpdateAppSetting

class AppSettingManager(
    private val getAppSetting: GetAppSetting,
    private val updateAppSetting: UpdateAppSetting
) {

    private val _isDarkTheme = MutableStateFlow(false)
    val isDarkTheme: StateFlow<Boolean> = _isDarkTheme

    var appSetting = AppSetting()
        private set

    init {
        CoroutineScope(Dispatchers.Default).launch {
            getAppSettingFlow().collectLatest {
                appSetting = it
                _isDarkTheme.emit(it.isDarkTheme)
            }
        }
    }

    fun update(appSetting: AppSetting) {
        updateAppSetting(appSetting)
    }

    fun getAppSettingFlow(): Flow<AppSetting> {
        return getAppSetting()
    }

    fun updateServerConfig(config: ServerConfig) {
        val configs = appSetting.serverConfigs.map {
            if (it.id == config.id) config else it
        }

        val updatedAppSetting = appSetting.copy(
            serverConfigs = configs
        )

        update(updatedAppSetting)
    }
}