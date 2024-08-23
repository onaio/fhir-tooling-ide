package org.smartregister.fct.sm.presentation.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import kotlinx.coroutines.launch
import org.smartregister.fct.aurora.presentation.ui.components.PanelHeading
import org.smartregister.fct.aurora.presentation.ui.components.TextButton
import org.smartregister.fct.common.data.locals.LocalSnackbarHost
import org.smartregister.fct.sm.presentation.component.TabComponent

@Composable
internal fun StructureMapControlPanel(component: TabComponent) {
    Column(
        modifier = Modifier.background(MaterialTheme.colorScheme.surfaceContainer).fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        val scope = rememberCoroutineScope()
        val snackbarHostState = LocalSnackbarHost.current
        val totalGroups by component.groups.subscribeAsState()
        val outputResources by component.outputResources.subscribeAsState()

        PanelHeading("Structure Map")

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween,
        ) {
            Column {
                Column(Modifier.padding(8.dp)) {
                    TransformButton(component)
                    AddSourceButton(component)

                }
                GroupListAndOutResources("Groups", false, totalGroups)
                GroupListAndOutResources("Output Resources", true, outputResources)
            }

            TextButton(modifier = Modifier.fillMaxWidth().padding(8.dp), label = "Save", onClick = {
                component.save()
                scope.launch {
                    snackbarHostState.showSnackbar("${component.smDetail.title} has been updated.")
                }
            })
        }
    }
}