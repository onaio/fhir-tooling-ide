package org.smartregister.fct

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.decompose.extensions.compose.lifecycle.LifecycleController
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import fct.composeapp.generated.resources.Res
import fct.composeapp.generated.resources.app_icon
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import org.koin.core.context.startKoin
import org.smartregister.fct.adb.ADBModuleSetup
import org.smartregister.fct.common.CommonModuleSetup
import org.smartregister.fct.common.data.locals.LocalRootComponent
import org.smartregister.fct.common.data.locals.LocalSnackbarHost
import org.smartregister.fct.common.data.locals.LocalSubWindowManager
import org.smartregister.fct.configs.ConfigModuleSetup
import org.smartregister.fct.fm.FileManagerModuleSetup
import org.smartregister.fct.pm.PMModuleSetup
import org.smartregister.fct.presentation.component.RootComponentImpl
import org.smartregister.fct.presentation.ui.App
import org.smartregister.fct.presentation.ui.MainWindow
import org.smartregister.fct.presentation.ui.components.BottomBar
import org.smartregister.fct.presentation.ui.components.TitleBar
import org.smartregister.fct.sm.SMModuleSetup
import org.smartregister.fct.util.runOnUiThread
import java.awt.Toolkit


fun main() = application {

    val screenSize = Toolkit.getDefaultToolkit().screenSize
    val screenWidth = screenSize.width
    val screenHeight = screenSize.height
    val scope = rememberCoroutineScope()
    val lifecycle = LifecycleRegistry()

    val windowState = rememberWindowState(
        position = WindowPosition.Aligned(Alignment.Center),
        width = (screenWidth - 300).dp,
        height = (screenHeight - 200).dp
    )


    val rootComponent = runOnUiThread {
        RootComponentImpl(
            componentContext = DefaultComponentContext(lifecycle = lifecycle)
        )
    }

    LifecycleController(lifecycle, windowState)

    startKoin { }

    initSubModules(scope)

    val subWindowViewModel = LocalSubWindowManager.current

    CompositionLocalProvider(LocalRootComponent provides rootComponent) {
        MainWindow(
            state = windowState,
            title = "FhirCore Toolkit",
            appIcon = painterResource(Res.drawable.app_icon),
            onCloseRequest = ::exitApplication,
            titleContent = {
                TitleBar(
                    componentContext = rootComponent,
                    subWindowManager = subWindowViewModel
                )
            },
        ) {
            Scaffold(
                snackbarHost = {
                    SnackbarHost(LocalSnackbarHost.current)
                },
                bottomBar = { BottomBar() },
                containerColor = Color.Transparent,
            ) {
                Box(modifier = Modifier.padding(it)) {
                    App(
                        rootComponent = rootComponent,
                        subWindowManager = subWindowViewModel
                    )
                }
            }
        }
    }
}

private fun initSubModules(scope: CoroutineScope) {

    listOf(
        CommonModuleSetup(),
        ConfigModuleSetup(),
        ADBModuleSetup(),
        PMModuleSetup(),
        FileManagerModuleSetup(),
        SMModuleSetup()
    ).forEach {
        scope.launch(Dispatchers.IO) {
            it.setup()
        }
    }
}