package org.smartregister.fct.configs.ui

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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import io.github.vinceglb.filekit.compose.rememberFilePickerLauncher
import io.github.vinceglb.filekit.core.PickerMode
import io.github.vinceglb.filekit.core.PickerType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.smartregister.fct.configs.data.viewmodel.ConfigManagerViewModel
import org.smartregister.fct.configs.data.viewmodel.ConfigTabViewModelContainer
import org.smartregister.fct.configs.data.viewmodel.RegisterConfigViewModel
import org.smartregister.fct.configs.domain.model.ConfigType
import org.smartregister.fct.configs.domain.model.ConfigWrapper
import org.smartregister.fct.configs.ui.components.ConfigTab
import org.smartregister.fct.configs.util.extension.flowAsState
import org.smartregister.fct.aurora.presentation.ui.components.Tab
import org.smartregister.fct.aurora.presentation.ui.components.TabRow
import org.smartregister.fct.common.util.compress

@Composable
fun ConfigManagerScreen() {

    val viewModel = remember { ConfigManagerViewModel() }
    val configTabViewModelList = ConfigTabViewModelContainer.tabViewModels
    var tabIndex by remember { mutableStateOf(0) }

    val configs = viewModel.getAllConfigs().flowAsState(viewModel.toString(), listOf())

    var showCreateConfigDialog by remember { mutableStateOf<String?>(null) }
    var showConfigDeleteDialog by remember { mutableStateOf<ConfigWrapper?>(null) }

    LaunchedEffect(configs.value.size) {
        if (!viewModel.isFirstTimeComposition) {
            tabIndex = if (configs.value.isNotEmpty()) configs.value.size - 1 else 0
        }
    }

    val launcher = rememberFilePickerLauncher(
        type = PickerType.File(),
        mode = PickerMode.Single
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            it?.readBytes()?.inputStream()?.bufferedReader()?.use { reader ->
                val encryptedJson = reader.readText().compress()
                showCreateConfigDialog = encryptedJson
            }
        }
    }

    configs.value.forEachIndexed { index, config ->
        if (!configTabViewModelList.containsKey(config.uuid)) {
            when (config.config.configType) {
                ConfigType.Register.name -> configTabViewModelList[config.uuid] =
                    RegisterConfigViewModel(config)
            }

        }
    }

    Column(modifier = Modifier.fillMaxSize()) {

        TabRow(
            modifier = Modifier.fillMaxWidth(),
            selectedTabIndex = tabIndex,
        ) {
            configs.value.forEachIndexed { index, tabConfig ->

                Tab(
                    text = {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                tabConfig.title,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Box(
                                modifier = Modifier
                                    .minimumInteractiveComponentSize()
                                    .clickable(
                                        onClick = { showConfigDeleteDialog = tabConfig },
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
                        text = "Upload New Config",
                        color = MaterialTheme.colorScheme.onSurface
                    )
                },
                selected = false,
                onClick = {
                    launcher.launch()
                }
            )
        }

        if (configs.value.isNotEmpty() && tabIndex < configs.value.size) {
            ConfigTabViewModelContainer.activeViewModel =
                ConfigTabViewModelContainer.tabViewModels[configs.value[tabIndex].uuid]!!
            ConfigTab(configs.value[tabIndex])
        }
    }

    if (showCreateConfigDialog != null) {
        Dialog(
            onDismissRequest = {}
        ) {

            var configTitle by remember { mutableStateOf("") }
            Surface(
                modifier = Modifier.width(300.dp),
                shape = RoundedCornerShape(8.dp)
            ) {
                Column(modifier = Modifier.padding(12.dp).fillMaxWidth()) {
                    TextField(
                        value = configTitle,
                        onValueChange = {
                            configTitle = it
                        }
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        Button(
                            onClick = {
                                showCreateConfigDialog = null
                            }
                        ) {
                            Text("Cancel")
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Button(
                            onClick = {
                                viewModel.insertConfig(configTitle, showCreateConfigDialog!!)
                                viewModel.isFirstTimeComposition = false
                                showCreateConfigDialog = null

                            }
                        ) {
                            Text("Upload")
                        }
                    }
                }
            }
        }
    }

    if (showConfigDeleteDialog != null) {
        AlertDialog(
            icon = {
                Icon(Icons.Filled.Warning, contentDescription = null)
            },
            title = {
                Text("Delete Config")
            },
            text = {
                Text("Are you sure you want to delete ${showConfigDeleteDialog!!.title} config")
            },
            onDismissRequest = { showConfigDeleteDialog = null },
            confirmButton = {
                TextButton(
                    onClick = {
                        configTabViewModelList.remove(showConfigDeleteDialog!!.uuid)
                        viewModel.deleteConfig(showConfigDeleteDialog!!.uuid)
                        showConfigDeleteDialog = null
                    }
                ) {
                    Text("Confirm")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showConfigDeleteDialog = null
                    }
                ) {
                    Text("Dismiss")
                }
            }
        )
    }
}