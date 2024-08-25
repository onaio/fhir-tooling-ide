package org.smartregister.fct.fhirman.presentation.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import org.smartregister.fct.aurora.presentation.ui.components.AutoCompleteDropDown
import org.smartregister.fct.aurora.presentation.ui.components.Button
import org.smartregister.fct.aurora.presentation.ui.components.HorizontalButtonStrip
import org.smartregister.fct.aurora.presentation.ui.components.OutlinedButton
import org.smartregister.fct.aurora.presentation.ui.components.OutlinedTextField
import org.smartregister.fct.common.data.manager.AuroraManager
import org.smartregister.fct.common.domain.model.ResizeOption
import org.smartregister.fct.common.presentation.ui.components.VerticalSplitPane
import org.smartregister.fct.engine.util.listOfAllFhirResources
import org.smartregister.fct.editor.ui.CodeEditor
import org.smartregister.fct.fhirman.data.manager.FhirmanServerTabsManager
import org.smartregister.fct.fhirman.presentation.components.FhirmanServerComponent
import org.smartregister.fct.fhirman.presentation.components.FhirmanServerTabComponent

context (FhirmanServerComponent, FhirmanServerTabsManager, AuroraManager)
@Composable
internal fun FhirmanServerTabComponent.ServerTab() {

    Column {

        Row(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.surfaceContainer)
                .padding(12.dp),
        ) {

            val initialSelectedConfig by selectedConfig.collectAsState()
            OutlinedButton(
                modifier = Modifier.height(40.dp),
                label = initialSelectedConfig?.title ?: "Select Config",
                onClick = {
                    selectServerConfig(initialSelectedConfig) {
                        println(it)
                        selectConfig(it)
                    }
                }
            )
            Spacer(Modifier.width(12.dp))
            HorizontalButtonStrip(
                options = options,
                label = { it.name },
                isExpanded = true,
                initialSelectedIndex = options.indexOf(content.methodType),
                stripBackgroundColor = MaterialTheme.colorScheme.background,
                key = this@ServerTab,
                onClick = {
                    content.methodType = this
                }
            )
        }

        HorizontalDivider()

        ConstraintLayout(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 8.dp)
        ) {

            val (resType, resId, btnSend) = createRefs()

            Box(modifier = Modifier
                .width(180.dp).constrainAs(resType) {
                    start.linkTo(parent.start)
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                }) {

                AutoCompleteDropDown(
                    modifier = Modifier.fillMaxWidth(),
                    items = listOfAllFhirResources,
                    label = { it },
                    heading = "Resource",
                    key = this@ServerTab,
                    defaultValue = content.resourceType,
                    onTextChanged = { text, isMatch ->
                        content.resourceType = text
                    }
                )
            }

            var resIdText by remember(this@ServerTab) { mutableStateOf(content.resourceId) }

            OutlinedTextField(
                modifier = Modifier.constrainAs(resId) {
                    start.linkTo(resType.end, margin = 8.dp)
                    top.linkTo(parent.top)
                    end.linkTo(btnSend.start, margin = 8.dp)
                    bottom.linkTo(parent.bottom)
                    width = Dimension.fillToConstraints
                },
                value = resIdText,
                onValueChange = {
                    content.resourceId = it
                    resIdText = it
                },
                label = "Id"
            )

            OutlinedButton(
                modifier = Modifier.constrainAs(btnSend) {
                    top.linkTo(parent.top)
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom)
                },
                label = "SEND",
                onClick = ::send
            )
        }

        HorizontalDivider()
        VerticalSplitPane(
            resizeOption = ResizeOption.Flexible(
                sizeRatio = 0.3f,
                minSizeRatio = 0.1f,
                maxSizeRatio = 0.9f,
            ),
            topContent = {
                CodeEditor(
                    modifier = Modifier.fillMaxSize(),
                    controller = content.bodyController,
                )
            },
            bottomContent = {
                CodeEditor(
                    modifier = Modifier.fillMaxSize(),
                    controller = content.responseController
                )
            }
        )
    }
}