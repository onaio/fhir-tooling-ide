package org.smartregister.fct.settings.presentation.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import org.smartregister.fct.serverconfig.presentation.components.ServerConfigPanelComponent
import org.smartregister.fct.serverconfig.presentation.ui.panel.ServerConfigPanel
import org.smartregister.fct.settings.domain.model.Setting
import org.smartregister.fct.settings.presentation.components.SettingsComponent

@Composable
internal fun SettingsContainer(
    component: SettingsComponent
) {

    val activeSetting by component.activeSetting.subscribeAsState()

    val serverConfigPanelComponent = remember {
        ServerConfigPanelComponent(component)
    }

    Row {
        SidePanel(
            activeSetting = activeSetting,
            onSettingClick = {
                component.changeSetting(it)
            }
        )

        when (activeSetting) {
            is Setting.ServerConfigs -> {
                with (serverConfigPanelComponent) {
                    ServerConfigPanel()
                }
            }
            is Setting.CodeEditor -> {}
        }

    }

}