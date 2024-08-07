package org.smartregister.fct.sm.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.screen.Screen
import io.github.vinceglb.filekit.compose.rememberFilePickerLauncher
import io.github.vinceglb.filekit.core.PickerMode
import io.github.vinceglb.filekit.core.PickerType
import kotlinx.coroutines.launch
import org.smartregister.fct.editor.data.controller.CodeController
import org.smartregister.fct.editor.data.enums.CodeStyle
import org.smartregister.fct.editor.ui.CodeEditor
import org.smartregister.fct.engine.ui.components.Tab
import org.smartregister.fct.engine.ui.components.TabRow
import org.smartregister.fct.engine.util.compress
import org.smartregister.fct.engine.util.uuid
import org.smartregister.fct.sm.data.provider.SMTabViewModelProvider
import org.smartregister.fct.sm.data.viewmodel.SMScreenViewModel
import org.smartregister.fct.sm.data.viewmodel.SMTabViewModel
import org.smartregister.fct.sm.domain.model.SMDetail

val LocalSMTabViewModelProvider = staticCompositionLocalOf { SMTabViewModelProvider() }

class StructureMapScreen : Screen {

    @Composable
    override fun Content() {

        val scope = rememberCoroutineScope()
        val viewModel = rememberScreenModel { SMScreenViewModel(scope) }
        val smTabViewModelProvider = LocalSMTabViewModelProvider.current.tabViewModels
        var tabIndex by remember { mutableStateOf(0) }

        val smDetailList = viewModel.getAllSMList().collectAsState(initial = listOf())

        var showCreateSMDialog by remember { mutableStateOf<String?>(null) }
        var showDeleteSMDialog by remember { mutableStateOf<SMDetail?>(null) }

        /*LaunchedEffect(smDetailList.value.size) {
            if (!viewModel.isFirstTimeComposition) {
                tabIndex = if (configs.value.isNotEmpty()) configs.value.size - 1 else 0
            }
        }*/

        val launcher = rememberFilePickerLauncher(
            type = PickerType.File(),
            mode = PickerMode.Single
        ) {
            scope.launch {
                it?.readBytes()?.inputStream()?.bufferedReader()?.use { reader ->
                    val encryptedData = reader.readText().compress()
                    showCreateSMDialog = encryptedData
                }
            }
        }

        smDetailList.value.forEach { smDetail ->
            if (!smTabViewModelProvider.containsKey(smDetail.id)) {
                smTabViewModelProvider[smDetail.id] = SMTabViewModel(scope)
            }
        }

        Column(modifier = Modifier.fillMaxSize()) {

            TabRow(
                modifier = Modifier.fillMaxWidth(),
                selectedTabIndex = tabIndex,
            ) {
                smDetailList.value.forEachIndexed { index, smDetail ->

                    Tab(
                        text = {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    smDetail.title,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Box(
                                    modifier = Modifier
                                        .minimumInteractiveComponentSize()
                                        .clickable(
                                            onClick = { showDeleteSMDialog = smDetail },
                                            interactionSource = remember { MutableInteractionSource() },
                                            indication = rememberRipple(
                                                bounded = false,
                                                radius = 15.dp
                                            )
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        modifier = Modifier.size(20.dp),
                                        imageVector = Icons.Rounded.Close,
                                        tint = MaterialTheme.colorScheme.onPrimaryContainer,
                                        contentDescription = null
                                    )
                                }
                            }
                        },
                        selected = index == tabIndex,
                        onClick = {
                            tabIndex = index
                        }
                    )
                }

                Tab(
                    text = {
                        Text(
                            text = "Upload New Structure Map",
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    },
                    selected = false,
                    onClick = {
                        launcher.launch()
                    }
                )
            }

            if (smDetailList.value.isNotEmpty() && tabIndex < smDetailList.value.size) {
                /*  ConfigTabViewModelContainer.activeViewModel =
                      ConfigTabViewModelContainer.tabViewModels[configs.value[tabIndex].uuid]!!*/
                //ConfigTab(configs.value[tabIndex])

                val controller = remember { mutableStateOf(CodeController()) }
                CodeEditor(
                    value = smDetailList.value[tabIndex].body,
                    codeStyle = CodeStyle.StructureMap,
                    controller = controller.value
                )
                val data = controller.value.getTextAsFlow().collectAsState()
                println(data.value)
            }
        }

        if (showCreateSMDialog != null) {
            Dialog(
                onDismissRequest = {}
            ) {

                var smTitle by remember { mutableStateOf("") }
                Surface(
                    modifier = Modifier.width(300.dp),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Column(modifier = Modifier.padding(12.dp).fillMaxWidth()) {
                        TextField(
                            value = smTitle,
                            onValueChange = {
                                smTitle = it
                            }
                        )
                        Spacer(modifier = Modifier.height(20.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End
                        ) {
                            Button(
                                onClick = {
                                    showCreateSMDialog = null
                                }
                            ) {
                                Text("Cancel")
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Button(
                                onClick = {
                                    viewModel.insert(
                                        SMDetail(
                                            id = uuid(),
                                            title = smTitle,
                                            body = showCreateSMDialog!!
                                        )
                                    )
                                    //viewModel.isFirstTimeComposition = false
                                    showCreateSMDialog = null

                                }
                            ) {
                                Text("Upload")
                            }
                        }
                    }
                }
            }
        }

        if (showDeleteSMDialog != null) {
            AlertDialog(
                icon = {
                    Icon(Icons.Filled.Warning, contentDescription = null)
                },
                title = {
                    Text("Delete Structure Map")
                },
                text = {
                    Text("Are you sure you want to delete ${showDeleteSMDialog!!.title} structure map")
                },
                onDismissRequest = { showDeleteSMDialog = null },
                confirmButton = {
                    TextButton(
                        onClick = {
                            smTabViewModelProvider.remove(showDeleteSMDialog!!.id)
                            viewModel.delete(showDeleteSMDialog!!.id)
                            showDeleteSMDialog = null
                        }
                    ) {
                        Text("Confirm")
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = {
                            showDeleteSMDialog = null
                        }
                    ) {
                        Text("Dismiss")
                    }
                }
            )
        }
    }

}