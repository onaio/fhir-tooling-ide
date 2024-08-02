package org.smartregister.fct.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Cyclone
import androidx.compose.material.icons.rounded.DarkMode
import androidx.compose.material.icons.rounded.Dataset
import androidx.compose.material.icons.rounded.Insights
import androidx.compose.material.icons.rounded.LightMode
import androidx.compose.material.icons.rounded.MoveDown
import androidx.compose.material.icons.rounded.Widgets
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.Navigator
import org.smartregister.fct.configs.ui.ConfigManagerScreen
import org.smartregister.fct.engine.data.locals.LocalFCTTheme
import org.smartregister.fct.ui.screen.structure_map.StructureMapScreen

@Composable
fun LeftNavigation(
    mainNavigator: Navigator?
) {
    Box(modifier = Modifier
        .width(60.dp)
        .fillMaxHeight()
        .background(MaterialTheme.colorScheme.surfaceContainer)
    ) {
        Row(
            modifier = Modifier.fillMaxSize()
        ) {
            NavigationBar(mainNavigator)
            VerticalDivider()
        }
    }
}

@Composable
private fun NavigationBar(mainNavigator: Navigator?) {
    Column(
        modifier = Modifier.fillMaxWidth().background(MaterialTheme.colorScheme.surface.copy(alpha = 0.2f)).fillMaxHeight(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            var selectedNav by remember { mutableStateOf(0) }
            HeightSpacer(12.dp)
            navigationMenu.forEachIndexed { index, navButton ->
                IconButton(
                    enabled = selectedNav != index,
                    onClick = {
                        navButton.onClick(mainNavigator!!)
                        selectedNav = index
                    },
                    colors = if(selectedNav == index) IconButtonDefaults.iconButtonColors(
                        disabledContainerColor = MaterialTheme.colorScheme.background,
                        disabledContentColor = MaterialTheme.colorScheme.onBackground
                    ) else  IconButtonDefaults.iconButtonColors()
                ) {
                    Icon(navButton.icon, contentDescription = null)
                }
            }
        }
        ThemeChangerButton()
    }
}

@Composable
private fun ThemeChangerButton() {
    val themeViewModel = LocalFCTTheme.current
    IconButton(
        onClick = {
            themeViewModel.switchTheme()
        }
    ) {
        val icon = if (themeViewModel.isLightTheme()) Icons.Rounded.DarkMode else Icons.Rounded.LightMode
        Icon(icon, contentDescription = null)
    }
}

private val navigationMenu = listOf(
    NavigationButton(
        title = "Manage Configuration",
        icon = Icons.Rounded.Widgets,
        onClick = {
            it.popUntilRoot()
        }
    ),
    NavigationButton(
        title = "Structure Map Transformation",
        icon = Icons.Rounded.Cyclone,
        onClick = {
            it.replaceUntilRoot(StructureMapScreen())
        }
    ),
    NavigationButton(
        title = "CQL Transformation",
        icon = Icons.Rounded.MoveDown,
        onClick = {}
    ),
    NavigationButton(
        title = "Database",
        icon = Icons.Rounded.Dataset,
        onClick = {}
    ),
    NavigationButton(
        title = "Insights",
        icon = Icons.Rounded.Insights,
        onClick = {}
    )
)

data class NavigationButton(
    val title: String,
    val icon: ImageVector,
    val onClick: (navigator: Navigator) -> Unit
)

fun Navigator.replaceUntilRoot(screen: Screen) {
    if (lastItem is ConfigManagerScreen) {
        push(screen)
    } else {
        replace(screen)
    }
}