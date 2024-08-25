package org.smartregister.fct.fhirman.presentation.ui.components

import androidx.compose.foundation.layout.BoxScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import org.koin.compose.koinInject
import org.smartregister.fct.aurora.presentation.ui.components.FloatingActionIconButton
import org.smartregister.fct.aurora.presentation.ui.components.OutlinedButton
import org.smartregister.fct.common.data.manager.AuroraManager
import org.smartregister.fct.common.presentation.ui.components.AuroraTabs
import org.smartregister.fct.common.presentation.ui.container.Aurora
import org.smartregister.fct.fhirman.data.manager.FhirmanServerTabsManager
import org.smartregister.fct.fhirman.presentation.components.FhirmanServerComponent


@Composable
fun ComponentContext.FhirmanServerPanel(auroraManager: AuroraManager) {

    val fhirmanServerComponent = remember { FhirmanServerComponent(this, auroraManager) }
    val tabsManager: FhirmanServerTabsManager = koinInject()

    with(fhirmanServerComponent) {
        val configs by configs.subscribeAsState()

        if (configs.isNotEmpty()) {
            with(tabsManager) {

                if (controller.getItems().isEmpty()) {
                    tabsManager.addNewTab(fhirmanServerComponent, "Untitled")
                }

                AuroraTabs(
                    tabsController = controller,
                    noContent = { CreateNewTab() }
                ) {
                    Aurora(
                        componentContext = this,
                        fab = {
                            NewServerTabWrapper {
                                FloatingActionIconButton(
                                    icon = Icons.Outlined.Add,
                                    onClick = it::show,
                                    containerColor = MaterialTheme.colorScheme.primary,
                                    contentColor = MaterialTheme.colorScheme.onPrimary
                                )
                            }
                        }
                    ) {
                        ServerTab()
                    }
                }
            }
        } else {
            NoConfig()
        }
    }
}

context (FhirmanServerTabsManager, FhirmanServerComponent, BoxScope)
@Composable
private fun CreateNewTab() {
    NewServerTabWrapper {
        OutlinedButton(
            modifier = Modifier.align(Alignment.Center),
            label = "Create New",
            onClick = it::show
        )
    }
}