package org.smartregister.fct.sm.presentation.ui.components

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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.arkivanov.decompose.ComponentContext
import kotlinx.coroutines.launch
import org.hl7.fhir.r4.model.Bundle
import org.hl7.fhir.r4.model.Resource
import org.hl7.fhir.r4.model.ResourceType
import org.smartregister.fct.aurora.presentation.ui.components.ExtendedFloatingActionButton
import org.smartregister.fct.aurora.presentation.ui.components.ScrollableTabRow
import org.smartregister.fct.aurora.presentation.ui.components.Tab
import org.smartregister.fct.aurora.presentation.ui.components.TextButton
import org.smartregister.fct.aurora.presentation.ui.components.container.Aurora
import org.smartregister.fct.common.presentation.ui.dialog.rememberResourceUploadDialog
import org.smartregister.fct.common.util.encodeResourceToString
import org.smartregister.fct.editor.ui.CodeEditor
import org.smartregister.fct.common.util.readableResourceName
import org.smartregister.fct.editor.data.controller.CodeController
import org.smartregister.fct.json.JsonStyle
import org.smartregister.fct.json.JsonTree
import org.smartregister.fct.json.JsonTreeView
import org.smartregister.fct.sm.data.enums.ResultType
import org.smartregister.fct.sm.data.enums.inverse
import org.smartregister.fct.sm.presentation.component.ResultTabComponent
import org.smartregister.fct.sm.presentation.component.StructureMapResultTabComponent

@Composable
internal fun SMTransformationResult(componentContext: ComponentContext, bundle: Bundle) {

    var activeTabIndex by remember { mutableStateOf(0) }

    val component = if (bundle.hasEntry()) StructureMapResultTabComponent(
        componentContext = componentContext,
        resource = bundle.entry[activeTabIndex].resource
    ) else null

    val smCodeController = component?.takeIf {
        it.resource.resourceType == ResourceType.StructureMap
    }?.codeController


    Aurora {
        Column(modifier = Modifier.fillMaxSize()) {

            ConstraintLayout(
                modifier = Modifier.fillMaxWidth(),
            ) {

                val (left, right) = createRefs()

                ScrollableTabRow(
                    modifier = Modifier.constrainAs(left) {
                        start.linkTo(parent.start)
                        top.linkTo(parent.top)
                        end.linkTo(right.start)
                        bottom.linkTo(parent.bottom)
                        width = Dimension.preferredWrapContent
                    },
                    selectedTabIndex = activeTabIndex,
                ) {
                    bundle.entry.forEachIndexed { index, entry ->
                        Tab(
                            selected = index == activeTabIndex,
                            title = entry.resource.readableResourceName,
                            onClick = {
                                activeTabIndex = index
                            }
                        )
                    }
                }

                Row(
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.surface)
                        .height(40.dp)
                        .constrainAs(right) {
                            top.linkTo(parent.top)
                            end.linkTo(parent.end)
                            bottom.linkTo(parent.bottom)
                        }
                ) {

                    if (smCodeController != null && activeTabIndex == 0) {
                        with(componentContext) {
                            UploadOnServerButton(smCodeController)
                            UploadOnDeviceButton(smCodeController)
                        }
                        Spacer(Modifier.width(12.dp))
                    }
                }
            }

            if (component != null) {
                with(component) {
                    Content()
                }
            }
        }
    }
}

context (ResultTabComponent)
@Composable
private fun Content() {
    var resultType by remember { mutableStateOf(ResultType.Json) }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        floatingActionButton = {
            ExtendedFloatingActionButton(
                label = resultType.inverse.label,
                icon = resultType.inverse.icon,
                onClick = {
                    resultType = resultType.inverse
                }
            )
        }
    ) {
        Box(Modifier.padding(it)) {

            when (resultType) {
                ResultType.Json -> {
                    CodeEditor(
                        controller = codeController
                    )
                }

                ResultType.Tree -> {
                    val jsonTree = JsonTree(
                        key = codeController,
                        json = codeController.getText()
                    )
                    JsonTreeView(
                        modifier = Modifier.fillMaxSize(),
                        tree = jsonTree,
                        style = JsonStyle(MaterialTheme.colorScheme)
                    )
                    LaunchedEffect(codeController) {
                        jsonTree.expandRoot()
                    }
                }
            }
        }
    }
}

@Composable
private fun ComponentContext.UploadOnServerButton(smCodeController: CodeController) {

    val resourceUploadDialog = rememberResourceUploadDialog(
        componentContext = this
    )

    TextButton(
        label = "Upload on Server",
        shape = RectangleShape,
        onClick = {

            resourceUploadDialog.show(smCodeController.getText())
        }
    )
}

@Composable
private fun ComponentContext.UploadOnDeviceButton(smCodeController: CodeController) {
    TextButton(
        label = "Upload on Device",
        shape = RectangleShape,
        onClick = {}
    )
}