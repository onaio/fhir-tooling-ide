package org.smartregister.fct.presentation.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Cyclone
import androidx.compose.material.icons.outlined.DataObject
import androidx.compose.material.icons.outlined.Dataset
import androidx.compose.material.icons.outlined.Folder
import androidx.compose.material.icons.outlined.LocalFireDepartment
import androidx.compose.material.icons.outlined.MoveDown
import androidx.compose.material.icons.outlined.Token
import androidx.compose.material.icons.outlined.Widgets
import androidx.compose.material.icons.rounded.DarkMode
import androidx.compose.material.icons.rounded.LightMode
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import org.koin.compose.koinInject
import org.smartregister.fct.aurora.presentation.ui.components.Icon
import org.smartregister.fct.common.domain.model.Config
import org.smartregister.fct.common.presentation.component.RootComponent
import org.smartregister.fct.engine.data.manager.AppSettingManager

@Composable
fun LeftNavigation(
    rootComponent: RootComponent
) {
    Box(
        modifier = Modifier.width(60.dp).fillMaxHeight()
            .background(MaterialTheme.colorScheme.surface)
    ) {
        NavigationBar(rootComponent)
    }
}

@Composable
private fun NavigationBar(rootComponent: RootComponent) {

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            var selectedNav by remember { mutableStateOf(0) }
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
    val appSettingManager = koinInject<AppSettingManager>()
    var appSetting = appSettingManager.appSetting

    IconButton(onClick = {
        appSetting = appSettingManager.appSetting.copy(
            isDarkTheme = !appSetting.isDarkTheme
        )
        appSettingManager.update(appSetting)
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
                it.changeSlot(Config.ConfigManagement)
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
        NavigationButton(title = "Fhirman",
            icon = Icons.Outlined.LocalFireDepartment,
            onClick = {
                it.changeSlot(Config.Fhirman)
            }),
    )
}

data class NavigationButton(
    val title: String,
    val icon: ImageVector,
    val onClick: (rootComponent: RootComponent) -> Unit
)