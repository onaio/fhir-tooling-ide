package org.smartregister.fct.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.Chip
import androidx.compose.material.ChipDefaults
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.minimumInteractiveComponentSize
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import fct.composeapp.generated.resources.Res
import fct.composeapp.generated.resources.github_icon
import io.github.alexzhirkevich.compottie.Compottie
import io.github.alexzhirkevich.compottie.rememberLottiePainter
import org.jetbrains.compose.resources.painterResource
import org.smartregister.fct.adb.domain.model.Device
import org.smartregister.fct.adb.domain.usecase.DeviceManager
import org.smartregister.fct.engine.data.enums.RightWindowState
import org.smartregister.fct.engine.data.viewmodel.SubWindowViewModel
import org.smartregister.fct.radiance.ui.components.getLottieFireComposition

@Composable
fun TopAppBar(subWindowViewModel: SubWindowViewModel) {

    Column(modifier = Modifier.background(MaterialTheme.colorScheme.surface).fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 6.dp, vertical = 0.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {

                Image(
                    modifier = Modifier.height(24.dp).padding(start = 12.dp),
                    painter = rememberLottiePainter(
                        composition = getLottieFireComposition(),
                        iterations = Compottie.IterateForever
                    ),
                    contentDescription = null
                )

                Spacer(Modifier.width(18.dp))
                DeviceSelectionMenu()
                Spacer(Modifier.width(10.dp))
                ActivePackageChip(subWindowViewModel = subWindowViewModel)
            }
            Row {
                Box(
                    modifier = Modifier
                        .minimumInteractiveComponentSize()
                        .clickable(
                            onClick = {},
                            interactionSource = remember { MutableInteractionSource() },
                            indication = rememberRipple(bounded = false, radius = 20.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        modifier = Modifier.size(20.dp),
                        painter = painterResource(
                            Res.drawable.github_icon
                        ),
                        colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onBackground),
                        contentDescription = null
                    )
                }
            }
        }
        HorizontalDivider(
            // color = MaterialTheme.colorScheme.background
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DeviceSelectionMenu() {

    val devices by DeviceManager.getAllDevices().collectAsState(initial = listOf(null))
    var expanded by remember { mutableStateOf(false) }
    var selectedValue by remember { mutableStateOf(devices[0]) }

    if (devices[0] == null) {
        selectedValue = null
        DeviceManager.setActiveDevice(null)
    } else {
        devices.filterNotNull()
            .map {
                it.getDeviceInfo().id
            }.run {
                if (selectedValue?.getDeviceInfo()?.id !in this) {
                    selectedValue = devices[0]
                    DeviceManager.setActiveDevice(selectedValue)
                }
            }
    }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = Modifier.wrapContentSize().padding(0.dp).pointerHoverIcon(PointerIcon.Hand)
    ) {

        val colors = OutlinedTextFieldDefaults.colors()
        val interactionSource = remember { MutableInteractionSource() }

        BasicTextField(
            value = selectedValue.getInfo(),
            onValueChange = { /*type = it*/ },
            readOnly = true,
            singleLine = true,
            textStyle = LocalTextStyle.current.copy(
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                lineHeight = TextUnit(10f, TextUnitType.Sp)
            ),
            enabled = false,
            modifier = Modifier.menuAnchor().wrapContentSize().height(32.dp)
                .pointerHoverIcon(PointerIcon.Hand),
            decorationBox = @Composable { innerTextField ->
                OutlinedTextFieldDefaults.DecorationBox(
                    value = selectedValue.getInfo(),
                    visualTransformation = VisualTransformation.None,
                    innerTextField = innerTextField,
                    //placeholder = placeholder,
                    //label = label,
                    //leadingIcon = leadingIcon,
                    contentPadding = PaddingValues(start = 8.dp),
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(
                            expanded = expanded
                        )
                    },
                    //prefix = prefix,
                    //suffix = suffix,
                    //supportingText = supportingText,
                    //singleLine = singleLine,
                    enabled = true,
                    singleLine = true,
                    //isError = isError,
                    interactionSource = interactionSource,
                    colors = colors,
                    container = {
                        OutlinedTextFieldDefaults.ContainerBox(
                            enabled = true,
                            isError = false,
                            interactionSource = interactionSource,
                            colors,
                            OutlinedTextFieldDefaults.shape,
                        )
                    }
                )
            }
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = {
                expanded = false
            }
        ) {
            devices.forEach { selectionOption ->
                DropdownMenuItem(
                    text = {
                        Text(text = selectionOption.getOptionName())
                    },
                    onClick = {
                        selectedValue = selectionOption
                        expanded = false
                        DeviceManager.setActiveDevice(selectedValue)
                    }
                )
            }
        }

    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun ActivePackageChip(subWindowViewModel: SubWindowViewModel) {

    val activePackage by DeviceManager.getActivePackage().collectAsState(initial = null)

    Chip(
        onClick = {
            subWindowViewModel.setRightWindowState(RightWindowState.PackageManager)
        },
        colors = ChipDefaults.chipColors(
            backgroundColor = MaterialTheme.colorScheme.tertiary
        )
    ) {
        Text(
            text = activePackage?.name ?: activePackage?.packageId ?: "Select Package",
            style = TextStyle(
                color = MaterialTheme.colorScheme.onTertiary,
                fontSize = MaterialTheme.typography.titleSmall.fontSize
            )
        )
    }
}

private fun Device?.getOptionName(): String {
    return this?.getDeviceInfo()?.model ?: "No Device"
}

private fun Device?.getInfo(): String {
    return this
        ?.getDeviceInfo()
        ?.let {
            "${it.model} (${it.id}) Android ${it.version}, API ${it.apiLevel}"
        } ?: "No Device"
}