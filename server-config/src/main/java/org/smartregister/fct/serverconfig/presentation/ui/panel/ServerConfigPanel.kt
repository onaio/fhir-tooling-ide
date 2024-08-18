package org.smartregister.fct.serverconfig.presentation.ui.panel

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import org.smartregister.fct.aurora.ui.components.CloseableTab
import org.smartregister.fct.aurora.ui.components.ScrollableTabRow
import org.smartregister.fct.aurora.ui.components.dialog.rememberConfirmationDialog
import org.smartregister.fct.aurora.ui.components.dialog.rememberSingleFieldDialog
import org.smartregister.fct.aurora.util.fileNameValidation
import org.smartregister.fct.serverconfig.presentation.ui.components.CreateOrImportConfig
import org.smartregister.fct.serverconfig.presentation.ui.components.MultiItemFloatingActionButton
import org.smartregister.fct.serverconfig.presentation.components.ServerConfigPanelComponent
import org.smartregister.fct.serverconfig.presentation.ui.components.Content

@Composable
fun ServerConfigPanel(component : ServerConfigPanelComponent) {


    val activeTabIndex by component.activeTabIndex.subscribeAsState()
    val serverConfigList by component.tabComponents.subscribeAsState()
    val deleteConfigDialog = deleteConfigDialog(component)
    val titleDialogController = titleDialogController(component)

    Column {

        if (serverConfigList.isNotEmpty()) {

            ScrollableTabRow(
                modifier = Modifier.fillMaxWidth(),
                selectedTabIndex = activeTabIndex,
            ) {
                serverConfigList
                    .map { it.serverConfig }
                    .forEachIndexed { index, item ->
                        CloseableTab(
                            index = index,
                            item = item,
                            title = { it.title },
                            selected = index == activeTabIndex,
                            onClick = {
                                component.changeTab(it)
                            },
                            onClose = {
                                deleteConfigDialog.show(
                                    title = "Delete Config",
                                    message = "Are you sure you want to delete ${item.title} config?",
                                    data = index
                                )
                            }
                        )
                    }
            }

            Box(Modifier.fillMaxSize()) {
                serverConfigList[activeTabIndex].Content()
                MultiItemFloatingActionButton(component, titleDialogController)
            }
        } else {
            CreateOrImportConfig(
                component = component,
                titleDialogController = titleDialogController
            )
        }
    }
}

@Composable
private fun titleDialogController(component: ServerConfigPanelComponent) = rememberSingleFieldDialog(
    title = "Config Title",
    maxLength = 30,
    validations = listOf(fileNameValidation)
) { title, _ ->
    component.createNewConfig(title)
}

@Composable
private fun deleteConfigDialog(component: ServerConfigPanelComponent) = rememberConfirmationDialog<Int> { _, tabIndex ->
    tabIndex?.let {
        component.closeTab(it)
    }
}