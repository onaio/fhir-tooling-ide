package org.smartregister.fct.sm.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import kotlinx.coroutines.launch
import org.hl7.fhir.r4.model.Bundle
import org.smartregister.fct.editor.data.enums.CodeStyle
import org.smartregister.fct.editor.ui.CodeEditor
import org.smartregister.fct.engine.data.helper.AppSettingProvide.getKoin
import org.smartregister.fct.engine.util.logicalId
import org.smartregister.fct.json.JsonStyle
import org.smartregister.fct.json.JsonTree
import org.smartregister.fct.json.JsonTreeView
import org.smartregister.fct.radiance.ui.components.ExtendedFloatingActionButton
import org.smartregister.fct.radiance.ui.components.ScrollableTabRow
import org.smartregister.fct.radiance.ui.components.Tab
import org.smartregister.fct.sm.data.enums.ResultType
import org.smartregister.fct.sm.data.viewmodel.SMViewModel

@Composable
internal fun SMTransformationResult(bundle: Bundle) {

    val scope = rememberCoroutineScope()
    val viewModel = getKoin().get<SMViewModel>()
    var tabIndex by remember { mutableStateOf(0) }

    bundle.entry.forEachIndexed { index, entry ->
        viewModel.addSMResultTabViewModel(entry.resource)

        LaunchedEffect(null) {
            if (index == 0) {
                viewModel.updateActiveSMResultTabViewModel(entry.resource.logicalId)
            }
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {

        ScrollableTabRow(
            modifier = Modifier.fillMaxWidth(),
            selectedTabIndex = tabIndex,
        ) {
            bundle.entry.forEachIndexed { index, entry ->

                Tab(
                    text = {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                entry.resource.resourceType.name,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    },
                    selected = index == tabIndex,
                    onClick = {
                        scope.launch {
                            viewModel.updateActiveSMResultTabViewModel(entry.resource.logicalId)
                            tabIndex = index
                        }
                    }
                )
            }
        }

        val activeSMResultTabViewModel by viewModel.getActiveSMResultTabViewModel().collectAsState()

        activeSMResultTabViewModel
            ?.let { smResultTabViewModel ->

                var resultType by remember { mutableStateOf(ResultType.Json) }

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    floatingActionButton = {
                        ExtendedFloatingActionButton(
                            label = if (resultType == ResultType.Json) ResultType.Tree.label else ResultType.Json.label,
                            icon = if (resultType == ResultType.Json) ResultType.Tree.icon else ResultType.Json.icon,
                            onClick = {
                                resultType =
                                    if (resultType == ResultType.Json) ResultType.Tree else ResultType.Json
                            }
                        )
                    }
                ) {
                    Box(Modifier.padding(it)) {

                        when (resultType) {
                            ResultType.Json -> {
                                CodeEditor(
                                    codeStyle = CodeStyle.Json,
                                    controller = smResultTabViewModel.codeController
                                )
                            }

                            ResultType.Tree -> {
                                val jsonTree = JsonTree(
                                    key = smResultTabViewModel,
                                    json = smResultTabViewModel.codeController.getText()
                                )
                                JsonTreeView(
                                    modifier = Modifier.fillMaxSize(),
                                    tree = jsonTree,
                                    style = JsonStyle(MaterialTheme.colorScheme)
                                )
                                LaunchedEffect(smResultTabViewModel) {
                                    jsonTree.expandRoot()
                                }
                            }
                        }
                    }
                }

            }
    }
}