package org.smartregister.fct

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import org.smartregister.fct.configs.ConfigModuleSetup
import org.smartregister.fct.configs.util.extension.flowAsState
import org.smartregister.fct.engine.ModuleSetup
import org.smartregister.fct.engine.data.locals.LocalFCTTheme
import org.smartregister.fct.engine.data.locals.LocalSnackbarHost
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

    initSubModules()

    Window(
        onCloseRequest = ::exitApplication,
        title = "FCT",
        state = windowState
    ) {

        val isLightMode = LocalFCTTheme.current.themeState().flowAsState(initial = true)

        FCTTheme(
            isLightMode = isLightMode.value
        ) {
            Scaffold(
                snackbarHost = {
                    SnackbarHost(LocalSnackbarHost.current)
                },
                topBar = { TopAppBar() },
                bottomBar = { BottomBar() }
            ) {
                Box(modifier = Modifier.padding(it)) {
                    App()
                }
            }
        }
    }
}

fun initSubModules() {
    listOf<ModuleSetup>(
        ConfigModuleSetup()
    ).forEach { it.setup() }
}