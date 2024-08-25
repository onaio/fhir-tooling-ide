package org.smartregister.fct.fhirman.presentation.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import org.smartregister.fct.aurora.presentation.ui.components.PanelHeading
import org.smartregister.fct.aurora.presentation.ui.components.TextButton
import org.smartregister.fct.engine.domain.model.ServerConfig
import org.smartregister.fct.fhirman.data.manager.FhirmanServerTabsManager
import org.smartregister.fct.fhirman.presentation.components.FhirmanServerComponent

context (FhirmanServerComponent, FhirmanServerTabsManager)
@Composable
internal fun ConfigPanel(configs: List<ServerConfig>) {

    Column {

        NewServerTabWrapper {
            TextButton(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp, vertical = 4.dp),
                label = "Create New",
                onClick = it::show
            )
        }

        HorizontalDivider()
        PanelHeading("Server Configs")

        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {

            item { Spacer(Modifier.height(8.dp)) }
            items(configs) { config ->
                TextButton(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp),
                    label = config.title,
                    textAlign = TextAlign.Start,
                    selected = selectedConfig.collectAsState().value?.id == config.id,
                    onClick = {
                        selectConfig(config)
                    }
                )
            }
        }
    }
}