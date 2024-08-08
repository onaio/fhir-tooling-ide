package org.smartregister.fct

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import org.koin.core.context.startKoin
import org.smartregister.fct.adb.ADBModuleSetup
import org.smartregister.fct.configs.ConfigModuleSetup
import org.smartregister.fct.configs.util.extension.flowAsState
import org.smartregister.fct.engine.EngineModuleSetup
import org.smartregister.fct.engine.data.helper.AppSettingProvide
import org.smartregister.fct.engine.data.locals.LocalAppSettingViewModel
import org.smartregister.fct.engine.data.locals.LocalSnackbarHost
import org.smartregister.fct.engine.data.locals.LocalSubWindowViewModel
import org.smartregister.fct.engine.domain.model.AppSetting
import org.smartregister.fct.pm.PMModuleSetup
import org.smartregister.fct.sm.SMModuleSetup
import org.smartregister.fct.ui.App
import org.smartregister.fct.ui.components.BottomBar
import org.smartregister.fct.ui.components.TopAppBar
import org.smartregister.fct.ui.theme.FCTTheme
import java.awt.Toolkit

fun main() = application {

    val screenSize = Toolkit.getDefaultToolkit().screenSize
    val screenWidth = screenSize.width
    val screenHeight = screenSize.height

    val windowState = rememberWindowState(
        position = WindowPosition.Aligned(Alignment.Center),
        width = (screenWidth - 300).dp,
        height = (screenHeight - 200).dp
    )

    startKoin { }

    initSubModules()

    Window(
        onCloseRequest = ::exitApplication,
        title = "FCT",
        state = windowState
    ) {

        val appSetting by AppSettingProvide.getAppSetting().flowAsState(initial = AppSetting())
        LocalAppSettingViewModel.current.appSetting = appSetting
        val subWindowViewModel = LocalSubWindowViewModel.current

        FCTTheme(
            isDarkModel = appSetting.isDarkTheme
        ) {
            Scaffold(
                snackbarHost = {
                    SnackbarHost(LocalSnackbarHost.current)
                },
                topBar = { TopAppBar(subWindowViewModel) },
                bottomBar = { BottomBar() }
            ) {
                Box(modifier = Modifier.padding(it)) {
                    App(subWindowViewModel)
                }
            }
        }
    }
}

fun initSubModules() {
    listOf(
        EngineModuleSetup(),
        ConfigModuleSetup(),
        ADBModuleSetup(),
        PMModuleSetup(),
        SMModuleSetup()
    ).forEach { it.setup() }
}