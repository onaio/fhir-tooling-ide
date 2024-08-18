package org.smartregister.fct.serverconfig.presentation.ui.components

import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Upload
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.lt.compose_views.menu_fab.MenuFabItem
import com.lt.compose_views.menu_fab.MenuFloatingActionButton
import org.smartregister.fct.aurora.domain.controller.SingleFieldDialogController
import org.smartregister.fct.aurora.ui.components.Icon
import org.smartregister.fct.serverconfig.presentation.components.ServerConfigPanelComponent

@Composable
internal fun BoxScope.MultiItemFloatingActionButton(
    component: ServerConfigPanelComponent,
    titleDialogController: SingleFieldDialogController
) {

    val menuItems = mutableStateListOf<MenuFabItem>().apply {
        add(CreateFabMenu("New Config", Icons.Filled.Add))
        add(CreateFabMenu("Import Config", Icons.Filled.Download))
        add(CreateFabMenu("Export Config", Icons.Filled.Upload))
    }

    MenuFloatingActionButton(
        modifier = Modifier.align(Alignment.BottomEnd).padding(bottom = 24.dp),
        srcIcon = Icons.Outlined.Add,
        fabBackgroundColor = MaterialTheme.colorScheme.tertiaryContainer,
        items = menuItems,
    ) {
        when (it.label) {
            "New Config" -> titleDialogController.show()
        }
    }
}

@Composable
private fun CreateFabMenu(
    label: String,
    icon: ImageVector,
) = MenuFabItem(
    fabBackgroundColor = MaterialTheme.colorScheme.tertiary,
    labelBackgroundColor = MaterialTheme.colorScheme.surface,
    labelTextColor = MaterialTheme.colorScheme.onSurface,
    label = label,
    icon = {
        Icon(
            modifier = Modifier.size(16.dp),
            icon = icon,
            tint = MaterialTheme.colorScheme.onTertiary
        )
    }
)