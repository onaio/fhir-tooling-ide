package org.smartregister.fct.presentation.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Cyclone
import androidx.compose.material.icons.outlined.DataObject
import androidx.compose.material.icons.outlined.Dataset
import androidx.compose.material.icons.outlined.Folder
import androidx.compose.material.icons.outlined.MoveDown
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.Token
import androidx.compose.material.icons.outlined.Widgets
import androidx.compose.material.icons.rounded.DarkMode
import androidx.compose.material.icons.rounded.LightMode
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.Navigator
import org.koin.compose.koinInject
import org.smartregister.fct.aurora.ui.components.Icon
import org.smartregister.fct.engine.domain.model.Config
import org.smartregister.fct.engine.presentation.component.RootComponent
import org.smartregister.fct.engine.presentation.viewmodel.AppSettingViewModel

@Composable
fun LeftNavigation(
    rootComponent: RootComponent
) {
    Box(
        modifier = Modifier.width(60.dp).fillMaxHeight()
            .background(MaterialTheme.colorScheme.surfaceContainer)
    ) {
        Row(
            modifier = Modifier.fillMaxSize()
        ) {
            NavigationBar(rootComponent)
            VerticalDivider()
        }
    }
}

@Composable
private fun NavigationBar(rootComponent: RootComponent) {

    Column(
        modifier = Modifier.fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.2f)).fillMaxHeight(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            var selectedNav by remember { mutableStateOf(0) }
            val scope = rememberCoroutineScope()
            Spacer(Modifier.height(12.dp))
            navigationMenu().forEachIndexed { index, navButton ->
                IconButton(
                    enabled = selectedNav != index, onClick = {
                        navButton.onClick(rootComponent)
                        selectedNav = index
                    }, colors = if (selectedNav == index) IconButtonDefaults.iconButtonColors(
                        disabledContainerColor = MaterialTheme.colorScheme.tertiaryContainer,
                        disabledContentColor = MaterialTheme.colorScheme.onTertiaryContainer
                    ) else IconButtonDefaults.iconButtonColors()
                ) {
                    Icon(icon = navButton.icon)

                }
            }
        }
        ThemeChangerButton()
    }
}

@Composable
private fun ThemeChangerButton() {
    val appSettingViewModel = koinInject<AppSettingViewModel>()
    var appSetting = appSettingViewModel.appSetting

    IconButton(onClick = {
        appSetting = appSetting.copy(
            isDarkTheme = !appSetting.isDarkTheme
        )
        appSettingViewModel.setAndUpdate(appSetting)
    }) {
        val icon = if (appSetting.isDarkTheme) Icons.Rounded.LightMode else Icons.Rounded.DarkMode
        Icon(icon = icon)
    }
}


private fun navigationMenu(): List<NavigationButton> {

    return listOf(
        NavigationButton(title = "Manage Configuration",
            icon = Icons.Outlined.Widgets,
            onClick = {
                it.changeSlot(Config.DataSpecification)
            }),
        NavigationButton(title = "Structure Map Transformation",
            icon = Icons.Outlined.MoveDown,
            onClick = {
                it.changeSlot(Config.StructureMap)
            }),
        NavigationButton(title = "Careplan Generation",
            icon = Icons.Outlined.Cyclone,
            onClick = {}),
        NavigationButton(title = "CQL Transformation", icon = Icons.Outlined.Token, onClick = {}),
        NavigationButton(title = "File Manager", icon = Icons.Outlined.Folder, onClick = {
            it.changeSlot(Config.FileManager)
        }),
        NavigationButton(title = "Database", icon = Icons.Outlined.Dataset, onClick = {}),
        NavigationButton(title = "FHIR Path Expression",
            icon = Icons.Outlined.DataObject,
            onClick = {}),
        NavigationButton(title = "Settings", icon = Icons.Outlined.Settings, onClick = {})
    )
}

data class NavigationButton(
    val title: String,
    val icon: ImageVector,
    val onClick: (rootComponent: RootComponent) -> Unit
)

fun Navigator.replaceUntilRoot(screen: Screen) {
    /* if (lastItem is ConfigManagerScreen) {
         push(screen)
     } else {
         replace(screen)
     }*/
}