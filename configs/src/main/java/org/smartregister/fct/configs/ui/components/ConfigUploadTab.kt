package org.smartregister.fct.configs.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
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
import org.smartregister.fct.configs.util.extension.compress

@Composable
fun ConfigUploadTab(viewModel: ConfigManagerViewModel) {

    var showCreateConfigDialog by remember { mutableStateOf<String?>(null) }

    val launcher = rememberFilePickerLauncher (
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


    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(
            onClick = {
                launcher.launch()
            }
        ) {
            Text("Upload Config")
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
}