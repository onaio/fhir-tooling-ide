package org.smartregister.fct.configs.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CopyAll
import androidx.compose.material.icons.rounded.DataObject
import androidx.compose.material.icons.rounded.Save
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import org.smartregister.fct.json.JsonTreeView
import org.smartregister.fct.json.JsonStyle
import org.smartregister.fct.json.JsonTree
import kotlinx.coroutines.launch
import org.smartregister.fct.configs.data.viewmodel.ConfigTabViewModelContainer
import org.smartregister.fct.configs.domain.model.ConfigType
import org.smartregister.fct.configs.domain.model.ConfigWrapper
import org.smartregister.fct.configs.domain.model.RegisterConfiguration
import org.smartregister.fct.common.util.encodeJson
import org.smartregister.fct.common.data.locals.LocalSnackbarHost
import org.smartregister.fct.aurora.ui.components.Button

@Composable
fun ConfigTab(configWrapper: ConfigWrapper) {

    val configViewModel = ConfigTabViewModelContainer.tabViewModels[configWrapper.uuid]!!
    var json by remember { mutableStateOf<String?>(null) }

    ConstraintLayout {

        val (body, footer) = createRefs()

        Box(modifier = Modifier.fillMaxSize().constrainAs(body){
            start.linkTo(parent.start)
            top.linkTo(parent.top)
            end.linkTo(parent.end)
            bottom.linkTo(footer.top)
            height = Dimension.preferredWrapContent
        }) {
            when (configWrapper.config.configType) {
                ConfigType.Register.name -> RegisterConfigTab(configViewModel.configWrapper.get())
            }
        }

        Box(modifier = Modifier.constrainAs(footer) {
            start.linkTo(parent.start)
            end.linkTo(parent.end)
            bottom.linkTo(parent.bottom)
        }) {
            HorizontalDivider()
            Row(
                modifier = Modifier.background(MaterialTheme.colorScheme.tertiary.copy(alpha = 0.02f)).fillMaxWidth().padding(8.dp),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                /*Row {
                    HeaderChip("App Id", configWrapper.config.appId)
                    WidthSpacer(20.dp)
                    HeaderChip("Config Type", configWrapper.config.configType)
                }*/
                CopyJsonButton(configWrapper)
                Spacer(Modifier.width(12.dp))
                ViewJSON(onClick = {
                    json = configWrapper.get<RegisterConfiguration>().encodeJson()
                })
                Spacer(Modifier.width(12.dp))
                Button(
                    label = "Save",
                    icon = Icons.Rounded.Save,
                    onClick = {}
                )
            }
        }
    }

    if (json != null) {

        var treeSwitch by remember { mutableStateOf(true) }

        val jsonTree = JsonTree(
            json = json!!
        )

        AlertDialog(
            modifier = Modifier.width(800.dp).fillMaxHeight(0.9f),
            title = {
                Text(configWrapper.title)
            },
            text = {
                if(treeSwitch) {
                    JsonTreeView(
                        modifier = Modifier.fillMaxSize(),
                        tree = jsonTree,
                        style = JsonStyle(MaterialTheme.colorScheme)
                    )
                    LaunchedEffect(null) {
                        jsonTree.expandRoot()
                    }
                } else {
                    OutlinedTextField(
                        modifier = Modifier.fillMaxSize(),
                        value = TextFieldValue(AnnotatedString(json!!)),
                        onValueChange = {},
                        readOnly = true,
                        textStyle = TextStyle(
                            fontFamily = FontFamily.Monospace
                        )
                    )
                }
            },
            onDismissRequest = {},
            confirmButton = {
                Row {
                    Spacer(Modifier.width(8.dp))
                    TextButton(onClick = {
                        treeSwitch = !treeSwitch
                    }) {
                        Text("Switch ${if (treeSwitch) "Text" else "Tree"}")
                    }
                    Spacer(Modifier.width(8.dp))
                    TextButton(onClick = {
                        jsonTree.expandAll()
                    }) {
                        Text("Expand All")
                    }
                    Spacer(Modifier.width(8.dp))
                    TextButton(onClick = {
                        jsonTree.collapseAll()
                    }) {
                        Text("Collapse All")
                    }
                    Spacer(Modifier.width(8.dp))
                    CopyJsonButton(configWrapper)
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        json = null
                    }
                ) {
                    Text("Dismiss")
                }
            },
            properties = DialogProperties(
                usePlatformDefaultWidth = false
            )
        )
    }
}

@Composable
private fun CopyJsonButton(configWrapper: ConfigWrapper) {
    val clipboard = LocalClipboardManager.current

    val snackbarHost = LocalSnackbarHost.current
    val scope = rememberCoroutineScope()

    Button(
        label = "Copy JSON",
        icon = Icons.Rounded.CopyAll,
        onClick = {
            clipboard.setText(AnnotatedString(configWrapper.encodeConfig()))
            scope.launch {
                snackbarHost.showSnackbar("Content Copied")
            }
        }
    )
}

@Composable
private fun ViewJSON(onClick: () -> Unit) {
    Button(
        label = "View JSON",
        icon = Icons.Rounded.DataObject,
        onClick = onClick
    )
}

@Composable
private fun HeaderChip(key: String, appId: String) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer
        ),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(end = 12.dp)
        ) {
            Box(
                modifier = Modifier.background(MaterialTheme.colorScheme.primary).padding(horizontal = 12.dp, vertical = 12.dp)
            ) {
                Text(key,
                    color = MaterialTheme.colorScheme.onPrimary,
                    style = MaterialTheme.typography.bodySmall
                )
            }
            Spacer(Modifier.width(8.dp))
            Text(appId,
                color = MaterialTheme.colorScheme.onSecondaryContainer,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}