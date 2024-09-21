package org.smartregister.fct.fhirman.presentation.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import org.smartregister.fct.aurora.presentation.ui.components.AutoCompleteDropDown
import org.smartregister.fct.aurora.presentation.ui.components.HorizontalButtonStrip
import org.smartregister.fct.aurora.presentation.ui.components.OutlinedButton
import org.smartregister.fct.aurora.presentation.ui.components.OutlinedTextField
import org.smartregister.fct.aurora.presentation.ui.components.Tooltip
import org.smartregister.fct.aurora.presentation.ui.components.TooltipPosition
import org.smartregister.fct.common.data.manager.AuroraManager
import org.smartregister.fct.common.domain.model.ResizeOption
import org.smartregister.fct.common.presentation.ui.components.HorizontalSplitPane
import org.smartregister.fct.editor.ui.CodeEditor
import org.smartregister.fct.engine.util.listOfAllFhirResources
import org.smartregister.fct.fhirman.data.manager.FhirmanServerTabsManager
import org.smartregister.fct.fhirman.presentation.components.FhirmanServerComponent
import org.smartregister.fct.fhirman.presentation.components.FhirmanServerTabComponent

context (FhirmanServerComponent, FhirmanServerTabsManager, AuroraManager)
@Composable
internal fun FhirmanServerTabComponent.ServerTab() {

    Column {

        ConstraintLayout(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surfaceContainer)
                .padding(horizontal = 12.dp, vertical = 8.dp)
        ) {

            val (btnResConfig, methodType, resType, resId, btnSend) = createRefs()

            val initialSelectedConfig by selectedConfig.collectAsState()
            OutlinedButton(
                modifier = Modifier.constrainAs(btnResConfig) {
                    start.linkTo(parent.start)
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                },
                label = initialSelectedConfig?.title ?: "Select Config",
                onClick = {
                    selectServerConfig(initialSelectedConfig) {
                        selectConfig(it)
                    }
                }
            )

            HorizontalButtonStrip(
                modifier = Modifier.width(400.dp).constrainAs(methodType) {
                    start.linkTo(btnResConfig.end, margin = 12.dp)
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    width = Dimension.preferredWrapContent
                },
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

            Box(modifier = Modifier
                .width(240.dp).constrainAs(resType) {
                    start.linkTo(methodType.end, margin = 12.dp)
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    width = Dimension.preferredWrapContent
                }) {

                AutoCompleteDropDown(
                    modifier = Modifier.fillMaxWidth(),
                    items = listOfAllFhirResources,
                    label = { it },
                    placeholder = "Resource",
                    key = this@ServerTab,
                    defaultValue = content.resourceType,
                    onTextChanged = { text, isMatch ->
                        content.resourceType = text
                    }
                )
            }

            var resIdText by remember(this@ServerTab) { mutableStateOf(content.resourceId) }

            OutlinedTextField(
                modifier = Modifier
                    .constrainAs(resId) {
                        start.linkTo(resType.end, margin = 12.dp)
                        top.linkTo(parent.top)
                        end.linkTo(btnSend.start, margin = 12.dp)
                        bottom.linkTo(parent.bottom)
                        width = Dimension.fillToConstraints
                    }
                    .onPreviewKeyEvent { keyEvent ->
                        when {
                            keyEvent.key == Key.Enter && keyEvent.type == KeyEventType.KeyUp -> {
                                send()
                                true
                            }
                            else -> false
                        }
                    },
                value = resIdText,
                onValueChange = {
                    content.resourceId = it
                    resIdText = it
                },
                label = "Id"
            )

            Tooltip(
                modifier = Modifier.constrainAs(btnSend) {
                    top.linkTo(parent.top)
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom)
                },
                tooltip = "Ctrl+Enter",
                tooltipPosition = TooltipPosition.Bottom(),
            ) {
                OutlinedButton(
                    label = "SEND",
                    onClick = ::send
                )
            }
        }

        HorizontalDivider()
        HorizontalSplitPane(
            resizeOption = ResizeOption.Flexible(
                sizeRatio = 0.5f,
                minSizeRatio = 0.1f,
                maxSizeRatio = 0.9f,
            ),
            leftContent = {
                CodeEditor(
                    modifier = Modifier.fillMaxSize(),
                    controller = content.bodyController,
                )
            },
            rightContent = {
                CodeEditor(
                    modifier = Modifier.fillMaxSize(),
                    controller = content.responseController
                )
            }
        )
    }
}