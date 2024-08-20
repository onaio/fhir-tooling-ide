package org.smartregister.fct.serverconfig.presentation.components

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.google.gson.FormattingStyle
import com.google.gson.GsonBuilder
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.smartregister.fct.common.data.manager.AppSettingManager
import org.smartregister.fct.common.domain.model.ServerConfig
import org.smartregister.fct.common.util.componentScope

class ExportConfigDialogComponent(
    componentContext: ComponentContext
) : KoinComponent, ComponentContext by componentContext {

    private val appSettingManager: AppSettingManager by inject()
    private val gson = GsonBuilder()
        .disableHtmlEscaping()
        .setFormattingStyle(FormattingStyle.PRETTY.withIndent(" ".repeat(4)))
        .create()

    private val _configs = MutableValue<List<ServerConfig>>(listOf())
    val configs: Value<List<ServerConfig>> = _configs

    private val _checkedConfigs = MutableValue<List<ServerConfig>>(listOf())
    val checkedConfigs: Value<List<ServerConfig>> = _checkedConfigs

    val exportedContent = MutableValue("")

    init {
        componentScope.launch {
            appSettingManager.getAppSettingFlow().collectLatest {
                _configs.value = it.serverConfigs
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

    fun export() {
        componentScope.launch {
            exportedContent.value = gson.toJson(
                checkedConfigs
                    .value
                    .map {
                        it.copy(
                            authToken = ""
                        )
                    }
            )
        }
    }
}