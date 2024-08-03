package org.smartregister.fct.pm.ui.components

import androidx.compose.foundation.ContextMenuArea
import androidx.compose.foundation.ContextMenuItem
import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.java.KoinJavaComponent
import org.smartregister.fct.adb.domain.model.Device
import org.smartregister.fct.adb.domain.model.PackageInfo
import org.smartregister.fct.adb.domain.usecase.DeviceManager
import org.smartregister.fct.engine.data.locals.LocalSnackbarHost
import org.smartregister.fct.engine.ui.components.MiddleEllipsisText
import org.smartregister.fct.engine.util.uuid
import org.smartregister.fct.pm.domain.usecase.DeletePackage
import org.smartregister.fct.pm.domain.usecase.GetAllPackages
import org.smartregister.fct.pm.domain.usecase.GetSavedPackages
import org.smartregister.fct.pm.domain.usecase.SaveNewPackage

@Composable
internal fun PackageTabs(device: Device?) {

    var tabIndex by remember { mutableStateOf(0) }

    Column(modifier = Modifier.fillMaxSize()) {
        Box(Modifier.fillMaxWidth()) {
            TabRow(
                modifier = Modifier.fillMaxWidth(),
                selectedTabIndex = tabIndex,
            ) {
                listOf("Saved Packages", "All Packages").forEachIndexed { index, title ->
                    Tab(
                        modifier = Modifier.height(35.dp).background(if (tabIndex == index) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface),
                        text = {
                            Text(
                                modifier = Modifier.height(22.dp),
                                text = title,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        },
                        selected = index == tabIndex,
                        onClick = {
                            tabIndex = index
                        }
                    )
                }
            }
        }

        when (tabIndex) {
            0 -> SavedPackageListContainer()
            1 -> device?.let { AppPackageListContainer(it) }
        }
    }
}

@Composable
private fun SavedPackageListContainer() {

    val getSavedPackages = KoinJavaComponent.getKoin().get<GetSavedPackages>()
    val allPackages by getSavedPackages().collectAsState(initial = listOf())
    val searchText = remember { mutableStateOf("") }
    val filteredPackages = allPackages.filter { it.packageId.contains(searchText.value) }

    SearchPackage(searchText)
    NoPackageFound(filteredPackages.isNotEmpty())
    PackageList(filteredPackages, true)
}

@Composable
private fun AppPackageListContainer(
    device: Device
) {

    val getAllPackage = KoinJavaComponent.getKoin().get<GetAllPackages>()
    val allPackages by getAllPackage(device).collectAsState(initial = listOf())
    val searchText = remember { mutableStateOf("") }
    val filteredPackages = allPackages.filter { it.packageId.contains(searchText.value) }

    SearchPackage(searchText)
    NoPackageFound(filteredPackages.isNotEmpty())
    PackageList(filteredPackages, false)
}

@Composable
private fun NoPackageFound(isFound: Boolean) {
    if (!isFound) {
        Text(
            modifier = Modifier.fillMaxWidth().alpha(0.5f).padding(top = 20.dp),
            textAlign = TextAlign.Center,
            text = "No Package(s) found",
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
private fun PackageList(
    packageList: List<PackageInfo>,
    isSavedPackage: Boolean
) {

    val snackbarHost = LocalSnackbarHost.current
    val saveNewPackage = KoinJavaComponent.getKoin().get<SaveNewPackage>()
    val deletePackage = KoinJavaComponent.getKoin().get<DeletePackage>()
    val state = rememberLazyListState()
    var savePackageId by remember { mutableStateOf<String?>(null) }

    Box(modifier = Modifier.fillMaxSize()) {

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            state = state
        ) {

            itemsIndexed(packageList) { index, packageInfo ->

                ContextMenuArea(
                    items = {
                        if (isSavedPackage) {
                            listOf(
                                ContextMenuItem("Delete") {
                                    packageInfo.id?.let { deletePackage(it) }
                                    DeviceManager.getActivePackage().value?.packageId?.let { pid ->
                                        if(packageInfo.packageId == pid) {
                                            DeviceManager.setActivePackage(packageInfo.copy(name = null))
                                        }
                                    }
                                }
                            )
                        } else {
                            listOf(
                                ContextMenuItem("Save") {
                                    savePackageId = packageInfo.packageId
                                }
                            )
                        }
                    }
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { DeviceManager.setActivePackage(packageInfo) }
                            .padding(vertical = 3.dp, horizontal = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            modifier = Modifier.width(30.dp),
                            text = "${index + 1}",
                            style = MaterialTheme.typography.bodyMedium
                        )

                        Column {
                            packageInfo.name?.let {
                                Text(
                                    text = it,
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                            MiddleEllipsisText(
                                text = packageInfo.packageId,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }

            }
        }

        VerticalScrollbar(
            modifier = Modifier.align(Alignment.CenterEnd).fillMaxHeight(),
            adapter = rememberScrollbarAdapter(
                scrollState = state
            )
        )
    }

    if(savePackageId != null) {
        Dialog(
            onDismissRequest = {}
        ) {

            var packageName by remember { mutableStateOf("") }
            Surface(
                modifier = Modifier.wrapContentWidth().defaultMinSize(minWidth = 100.dp),
                shape = RoundedCornerShape(8.dp)
            ) {
                Column(modifier = Modifier.padding(12.dp).fillMaxWidth()) {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = "Save New Package",
                        style = MaterialTheme.typography.titleLarge,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(30.dp))
                    Text(
                        text = "Package Id: $savePackageId",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    OutlinedTextField(
                        modifier = Modifier.fillMaxWidth(),
                        value = packageName,
                        onValueChange = {
                            packageName = it
                        }
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        Button(
                            onClick = {
                                savePackageId = null
                            }
                        ) {
                            Text("Cancel")
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Button(
                            onClick = {
                                val id = uuid()
                                saveNewPackage(
                                    id = id,
                                    packageId = savePackageId!!,
                                    packageName = packageName
                                )
                                DeviceManager.getActivePackage().value?.packageId?.let { pid ->
                                    if(savePackageId == pid) {
                                        DeviceManager.setActivePackage(
                                            PackageInfo(
                                                packageId = savePackageId!!,
                                                id = id,
                                                name = packageName
                                            )
                                        )
                                    }
                                }
                                CoroutineScope(Dispatchers.IO).launch {
                                    snackbarHost.showSnackbar("Package $packageName has been saved")
                                }
                                savePackageId = null
                            }
                        ) {
                            Text("Save")
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SearchPackage(searchText: MutableState<String>) {

    BasicTextField(
        value = searchText.value,
        modifier = Modifier.fillMaxWidth(),
        onValueChange = { searchText.value = it},
        textStyle = TextStyle(
            color = MaterialTheme.colorScheme.onBackground,
            fontSize = MaterialTheme.typography.bodyMedium.fontSize
        ),
        decorationBox = @Composable { innerTextField ->
            TextFieldDefaults.DecorationBox(
                value = searchText.value,
                visualTransformation = VisualTransformation.None,
                innerTextField = innerTextField,
                placeholder = {
                    Text(
                        text = "Search",
                        style = MaterialTheme.typography.bodyMedium
                    )
                },
                singleLine = true,
                enabled = true,
                isError = false,
                interactionSource = remember { MutableInteractionSource() },
                contentPadding = PaddingValues(8.dp),
            )
        }
    )
}