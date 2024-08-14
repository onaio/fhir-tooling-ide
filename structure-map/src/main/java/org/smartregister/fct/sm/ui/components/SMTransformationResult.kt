package org.smartregister.fct.sm.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import kotlinx.coroutines.launch
import org.hl7.fhir.r4.model.Bundle
import org.smartregister.fct.aurora.ui.components.ExtendedFloatingActionButton
import org.smartregister.fct.aurora.ui.components.ScrollableTabs
import org.smartregister.fct.editor.data.enums.FileType
import org.smartregister.fct.editor.ui.CodeEditor
import org.smartregister.fct.engine.data.helper.AppSettingProvide.getKoin
import org.smartregister.fct.engine.util.logicalId
import org.smartregister.fct.json.JsonStyle
import org.smartregister.fct.json.JsonTree
import org.smartregister.fct.json.JsonTreeView
import org.smartregister.fct.sm.data.enums.ResultType
import org.smartregister.fct.sm.data.viewmodel.SMViewModel

@Composable
internal fun SMTransformationResult(bundle: Bundle) {

    val scope = rememberCoroutineScope()
    val viewModel = getKoin().get<SMViewModel>()
    //var tabIndex by remember { mutableStateOf(0) }

    bundle.entry.forEachIndexed { index, entry ->
        viewModel.addSMResultTabViewModel(entry.resource)

        LaunchedEffect(null) {
            if (index == 0) {
                viewModel.updateActiveSMResultTabViewModel(entry.resource.logicalId)
            }
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {

        ScrollableTabs<Bundle.BundleEntryComponent>(
            tabs = bundle.entry,
            title = { it.resource.resourceType.name },
            onSelected = { _, item ->
                scope.launch {
                    viewModel.updateActiveSMResultTabViewModel(item.resource.logicalId)
                }
            }
        )

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
                                    fileType = FileType.Json,
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