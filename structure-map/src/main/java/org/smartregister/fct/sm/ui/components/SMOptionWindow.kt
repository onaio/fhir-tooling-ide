package org.smartregister.fct.sm.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Link
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
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
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.hl7.fhir.r4.model.Bundle
import org.hl7.fhir.r4.model.Resource
import org.koin.java.KoinJavaComponent.getKoin
import org.smartregister.fct.aurora.ui.components.OutlinedButton
import org.smartregister.fct.aurora.ui.components.TextButton
import org.smartregister.fct.aurora.ui.components.dialog.DialogType
import org.smartregister.fct.aurora.ui.components.dialog.getOrDefault
import org.smartregister.fct.aurora.ui.components.dialog.rememberDialog
import org.smartregister.fct.aurora.ui.components.dialog.rememberLoaderDialogController
import org.smartregister.fct.editor.data.enums.FileType
import org.smartregister.fct.engine.util.encodeResourceToString
import org.smartregister.fct.engine.util.listOfAllFhirResources
import org.smartregister.fct.engine.util.prettyJson
import org.smartregister.fct.fm.ui.dialog.rememberFileProviderDialog
import org.smartregister.fct.logger.FCTLogger
import org.smartregister.fct.sm.data.transformation.SMTransformService
import org.smartregister.fct.sm.data.viewmodel.SMTabViewModel
import org.smartregister.fct.sm.data.viewmodel.SMViewModel

@Composable
fun SMOptionWindow() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        val scope = rememberCoroutineScope()
        val viewModel = getKoin().get<SMViewModel>()
        val activeSMTabViewModel by viewModel.getActiveSMTabViewModel()
            .collectAsState()
        val smText by activeSMTabViewModel?.codeController?.getTextAsFlow()?.collectAsState()
            ?: remember { mutableStateOf("") }

        var totalGroups by remember { mutableStateOf(listOf<String>()) }
        var outputResources by remember { mutableStateOf(listOf<String>()) }

        LaunchedEffect(smText.length) {
            totalGroups = getTotalGroups(smText)
            outputResources = getOutputResources(smText)
        }


        Box(
            Modifier.fillMaxWidth().height(40.dp)
                .background(MaterialTheme.colorScheme.surfaceContainer)
        ) {
            Text(
                modifier = Modifier.align(Alignment.Center),
                text = "Structure Map",
                style = MaterialTheme.typography.titleSmall
            )
            VerticalDivider(modifier = Modifier.align(Alignment.CenterEnd))
            HorizontalDivider(modifier = Modifier.align(Alignment.BottomCenter))
        }

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween,
        ) {
            Column {
                Column(Modifier.padding(8.dp)) {
                    TransformButtonWithAction(scope, activeSMTabViewModel, viewModel)
                    InputResourceButton(scope, activeSMTabViewModel)

                }
                GroupListAndOutResources("Groups", false, totalGroups)
                GroupListAndOutResources("Output Resources", true, outputResources)
            }

            CreateNewSMButton(
                modifier = Modifier.fillMaxWidth().padding(8.dp),
                label = "Create New Map",
                icon = null,
            )
        }
    }
}

@Composable
private fun TransformButtonWithAction(
    scope: CoroutineScope,
    smTabViewModel: SMTabViewModel?,
    viewModel: SMViewModel
) {
    val inputResource by smTabViewModel?.inputResource?.collectAsState()
        ?: remember { mutableStateOf<Resource?>(null) }


    val loaderController = rememberLoaderDialogController()

    val errorDialogController = rememberDialog(
        title = "Transformation Error",
        dialogType = DialogType.Error
    ) {
        Text(
            modifier = Modifier.fillMaxWidth().padding(12.dp),
            text = it.getExtra().getOrDefault(0, ""),
            textAlign = TextAlign.Center
        )
    }

    val resultDialogController = rememberDialog(
        title = "Transformation Result",
        width = 1200.dp,
        height = 800.dp,
    ) {
        viewModel.clearAllSMResultTabViewModel()
        SMTransformationResult(it.getExtra().getOrDefault(0, Bundle()))
    }

    OutlinedButton(
        modifier = Modifier.fillMaxWidth(),
        label = "Transform",
        enable = smTabViewModel != null,
        onClick = {

            smTabViewModel?.let { smTabViewModel ->
                loaderController.show()
                scope.launch(Dispatchers.IO) {
                    val result = viewModel.applyTransformation(smTabViewModel, inputResource)
                    loaderController.hide()

                    if (result.isSuccess) {
                        resultDialogController.show(result.getOrThrow())
                    } else {
                        errorDialogController.show(result.exceptionOrNull()?.message)
                    }
                }
            }
        }
    )
}

@Composable
private fun InputResourceButton(scope: CoroutineScope, smTabViewModel: SMTabViewModel?) {
    val inputResource by smTabViewModel?.inputResource?.collectAsState()
        ?: remember { mutableStateOf<Resource?>(null) }

    val parsingErrorDialog = rememberDialog(
        title = "Parsing Error",
        width = 300.dp,
        dialogType = DialogType.Error
    ) {
        Text(
            modifier = Modifier.fillMaxWidth().padding(12.dp),
            text = it.getExtra().getOrDefault(0, ""),
            textAlign = TextAlign.Center
        )
    }

    val filePickerDialog = rememberFileProviderDialog(
        fileType = FileType.Json
    ) { source ->
        scope.launch {
            val result = smTabViewModel?.addSource(source)
            if (result?.isFailure == true) {
                parsingErrorDialog.show(result.exceptionOrNull()?.message)
            }
        }
    }

    TextButton(
        modifier = Modifier.fillMaxWidth(),
        label = inputResource?.resourceType?.name ?: "Add Source",
        enable = smTabViewModel != null,
        onClick = {
            scope.launch {
                val title = inputResource?.resourceType?.name ?: "Source"
                val initialData = inputResource?.prettyJson() ?: ""
                filePickerDialog.show(title, initialData)
            }
        }
    )
}

@Composable
private fun GroupListAndOutResources(title: String, showLinkIcon: Boolean, items: List<String>) {

    Box(
        Modifier.fillMaxWidth().height(35.dp)
            .background(MaterialTheme.colorScheme.surfaceContainer)
    ) {
        HorizontalDivider(modifier = Modifier.align(Alignment.TopCenter))
        Text(
            modifier = Modifier.align(Alignment.Center),
            text = title,
            style = MaterialTheme.typography.titleSmall
        )
        HorizontalDivider(modifier = Modifier.align(Alignment.BottomCenter))
    }

    Box {

        val scrollState = rememberScrollState()
        val uriHandler = LocalUriHandler.current

        LazyColumn(Modifier.verticalScroll(scrollState).heightIn(min = 50.dp, max = 145.dp)) {
            items(items) { item ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        modifier = Modifier.fillMaxWidth(if (showLinkIcon) 0.85f else 1f)
                            .clickable {}.padding(horizontal = 8.dp, vertical = 4.dp),
                        text = item,
                        overflow = TextOverflow.Ellipsis,
                        softWrap = false,
                        style = TextStyle(
                            fontFamily = FontFamily.Monospace,
                            fontSize = MaterialTheme.typography.labelMedium.fontSize
                        )
                    )

                    if (showLinkIcon) {
                        Image(
                            modifier = Modifier.width(20.dp).padding(end = 5.dp).clickable {
                                uriHandler.openUri("https://hl7.org/fhir/R4B/${item.lowercase()}.html")
                            },
                            imageVector = Icons.Outlined.Link,
                            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurface),
                            contentDescription = null
                        )
                    }
                }

            }
        }

        VerticalScrollbar(
            modifier = Modifier.align(Alignment.CenterEnd).heightIn(min = 50.dp, max = 145.dp),
            adapter = rememberScrollbarAdapter(
                scrollState = scrollState
            )
        )
    }

}

private fun getTotalGroups(smText: String): List<String> {

    return "(?<=(\\n|\\r|\\s|\\})group\\s)\\w+(?=\\s*\\()"
        .toRegex()
        .findAll(smText)
        .map { it.value }
        .toList()
}

private fun getOutputResources(smText: String): List<String> {
    return "(?<=['\"])\\w+(?=(['\"])\\s*\\)\\s*as)"
        .toRegex()
        .findAll(smText)
        .filter { it.value in listOfAllFhirResources }
        .map { it.value }.toList()
}