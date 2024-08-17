package org.smartregister.fct.sm.presentation.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.ComponentContext
import org.hl7.fhir.r4.model.Bundle
import org.smartregister.fct.aurora.ui.components.ExtendedFloatingActionButton
import org.smartregister.fct.aurora.ui.components.ScrollableTabRow
import org.smartregister.fct.aurora.ui.components.Tab
import org.smartregister.fct.editor.ui.CodeEditor
import org.smartregister.fct.common.util.readableResourceName
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

    Column(modifier = Modifier.fillMaxSize()) {

        ScrollableTabRow(
            modifier = Modifier.fillMaxWidth(),
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

        if (bundle.hasEntry()) {
            Content(
                StructureMapResultTabComponent(
                    componentContext = componentContext,
                    resource = bundle.entry[activeTabIndex].resource
                )
            )
        }
    }
}

@Composable
private fun Content(component: ResultTabComponent) {
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
                        controller = component.codeController
                    )
                }

                ResultType.Tree -> {
                    val jsonTree = JsonTree(
                        key = component,
                        json = component.codeController.getText()
                    )
                    JsonTreeView(
                        modifier = Modifier.fillMaxSize(),
                        tree = jsonTree,
                        style = JsonStyle(MaterialTheme.colorScheme)
                    )
                    LaunchedEffect(component) {
                        jsonTree.expandRoot()
                    }
                }
            }
        }
    }
}