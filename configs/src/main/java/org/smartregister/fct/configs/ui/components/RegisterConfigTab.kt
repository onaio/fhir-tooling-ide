package org.smartregister.fct.configs.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import org.hl7.fhir.r4.model.ResourceType
import org.smartregister.fct.configs.data.helper.ConfigComponentState
import org.smartregister.fct.configs.data.viewmodel.ComponentNavigator
import org.smartregister.fct.configs.data.viewmodel.ConfigTabViewModelContainer
import org.smartregister.fct.configs.data.viewmodel.RegisterConfigViewModel
import org.smartregister.fct.configs.domain.model.FhirResourceConfig
import org.smartregister.fct.configs.domain.model.RegisterConfiguration
import org.smartregister.fct.configs.domain.model.ResourceConfig
import org.smartregister.fct.configs.util.extension.flowAsState
import org.smartregister.fct.aurora.presentation.ui.components.MiddleEllipsisText
import org.smartregister.fct.aurora.presentation.ui.components.Button
import org.smartregister.fct.logger.FCTLogger

@Composable
fun RegisterConfigTab(config: RegisterConfiguration) {

    val viewModel = ConfigTabViewModelContainer.get<RegisterConfigViewModel>()
    val fhirResourceNavigator = viewModel.fhirResourceNavigator
    val secondaryResourcesNavigator = viewModel.secondaryResourcesNavigator

    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(start = 12.dp, end = 12.dp)
    ) {

        item {
            Row(
                modifier = Modifier.fillMaxWidth().padding(top = 12.dp)
            )
            {
                Box(modifier = Modifier.weight(1f).padding(end = 6.dp)) {
                    FhirResourceContainer(config.fhirResource, fhirResourceNavigator) {
                        config.fhirResource = FhirResourceConfig()
                        fhirResourceNavigator.updateCurrentState(
                            ConfigComponentState.FhirResource(
                                fhirResourceConfig = config.fhirResource,
                                navigator = fhirResourceNavigator
                            )
                        )
                    }
                }

                Box(modifier = Modifier.weight(1f).padding(start = 6.dp)) {
                    SecondaryResourcesContainer(
                        config.secondaryResources,
                        secondaryResourcesNavigator
                    ) {
                        config.secondaryResources = mutableListOf()
                        secondaryResourcesNavigator.updateCurrentState(
                            ConfigComponentState.SecondaryResources(
                                secondaryResources = config.secondaryResources,
                                navigator = secondaryResourcesNavigator
                            )
                        )
                    }
                }
            }

            Spacer(Modifier.height(20.dp))
        }
    }
}

@Composable
fun FhirResourceContainer(
    fhirResourceConfig: FhirResourceConfig?,
    navigator: ComponentNavigator,
    onAdd: () -> Unit
) {
    ResourceContainer(
        initialState = navigator.getLastStateFromHistory() ?: ConfigComponentState.FhirResource(
            fhirResourceConfig = fhirResourceConfig,
            navigator = navigator
        ),
        navigator = navigator,
        onAdd = onAdd
    )
}

@Composable
fun SecondaryResourcesContainer(
    secondaryResources: MutableList<FhirResourceConfig>?,
    navigator: ComponentNavigator,
    onAdd: () -> Unit
) {
    ResourceContainer(
        initialState = navigator.getLastStateFromHistory()
            ?: ConfigComponentState.SecondaryResources(
                secondaryResources = secondaryResources,
                navigator = navigator
            ),
        navigator = navigator,
        onAdd = onAdd
    )
}

@Composable
fun ResourceContainer(
    initialState: ConfigComponentState,
    navigator: ComponentNavigator,
    onAdd: () -> Unit
) {

    val viewStateHistory = navigator.getStateHistory()
    val currentState = navigator.getState()
        .flowAsState(key = navigator.toString(), initial = initialState)

    if (currentState.value == null) {
        navigator.pushState(initialState)
    }

    val check = if (viewStateHistory.size > 1) true else currentState.value?.config != null

    if (currentState.value != null && check) {

        var expanded by remember(navigator.toString()) { mutableStateOf(navigator.isContainerExpanded) }
        val rotationState by animateFloatAsState(
            targetValue = if (expanded) 0f else 180f
        )

        Card(
            modifier = Modifier.fillMaxWidth().animateContentSize(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer
            )
        ) {

            Box(
                modifier = Modifier.fillMaxWidth().height(50.dp)
                    .background(MaterialTheme.colorScheme.secondary)
                    .padding(horizontal = 12.dp),
            ) {

                if (viewStateHistory.size > 1) {
                    SmallIconButton(
                        modifier = Modifier.align(Alignment.CenterStart),
                        icon = Icons.AutoMirrored.Rounded.ArrowBack,
                        onClick = {
                            navigator.popState()
                        }
                    )
                }

                Row(
                    modifier = Modifier.fillMaxSize(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            initialState.title,
                            color = MaterialTheme.colorScheme.onSecondary,
                            style = MaterialTheme.typography.titleMedium
                        )
                        MiddleEllipsisText(
                            modifier = Modifier.padding(horizontal = 40.dp),
                            text = viewStateHistory.joinToString(" > ") { it.title },
                            color = MaterialTheme.colorScheme.onSecondary,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }

                Row(
                    modifier = Modifier.fillMaxSize(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.End
                ) {
                    SmallIconButton(
                        modifier = Modifier.rotate(rotationState),
                        icon = Icons.Default.ArrowDropUp,
                        onClick = {
                            expanded = !expanded
                            navigator.setContainerExpanded(expanded)
                        }
                    )
                }
            }

            AnimatedVisibility(
                visible = expanded
            ) {
                Spacer(Modifier.height(8.dp))
                Column(modifier = Modifier.fillMaxWidth().padding(12.dp)) {
                    when (val state = currentState.value) {
                        is ConfigComponentState.FhirResource -> FhirResourceView(
                            state.fhirResourceConfig!!,
                            state.navigator
                        )

                        is ConfigComponentState.BaseResource -> BaseResourceView(state.baseResource)
                        is ConfigComponentState.SecondaryResources -> SecondaryResourcesView(
                            state.secondaryResources!!,
                            state.navigator
                        )

                        else -> FCTLogger.e("state $state is not valid")
                    }
                }
            }

        }
    } else {
        CreateButton(
            modifier = Modifier.fillMaxWidth(),
            text = "Add ${initialState.title}",
            onClick = onAdd
        )
    }
}

@Composable
fun FhirResourceView(fhirResourceConfig: FhirResourceConfig, navigator: ComponentNavigator) {

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        BaseResourceViewButton(
            fhirResourceConfig,
            navigator,
            modifier = Modifier.weight(1f).padding(end = 6.dp)
        )
        RelatedResourceListView(
            fhirResourceConfig.baseResource,
            modifier = Modifier.weight(1f).padding(start = 6.dp)
        )
    }

}

@Composable
fun BaseResourceViewButton(
    config: FhirResourceConfig,
    navigator: ComponentNavigator,
    modifier: Modifier = Modifier
) {

    config.baseResource?.let {
        Button(
            label = "Base Resource",
            modifier = modifier,
            onClick = {
                navigator.pushState(
                    ConfigComponentState.BaseResource(
                        baseResource = config.baseResource,
                        navigator = navigator
                    )
                )
            }
        )
    } ?: run {
        CreateButton(
            modifier = modifier,
            text = "Add Base Resource",
            onClick = {
                config.baseResource = ResourceConfig(
                    resource = ResourceType.Patient
                )
                navigator.pushState(
                    ConfigComponentState.BaseResource(
                        baseResource = config.baseResource,
                        navigator = navigator
                    )
                )
            }
        )
    }
}

@Composable
fun RelatedResourceListView(resourceConfig: ResourceConfig?, modifier: Modifier = Modifier) {

    Button(
        label = "Related Resource List",
        modifier = modifier,
        onClick = {}
    )
}

@Composable
fun BaseResourceView(baseResource: ResourceConfig?) {

    baseResource?.let {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Id", modifier = Modifier.weight(0.4f))
                IdFieldView(it.id, Modifier.weight(1.6f))
            }
            Spacer(Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Resource", modifier = Modifier.weight(0.4f))
                ResourceTypeDropdownView(it, Modifier.weight(1.6f))
            }
        }
    } ?: run {

    }

}

@Composable
fun SecondaryResourcesView(
    secondaryResources: MutableList<FhirResourceConfig>,
    navigator: ComponentNavigator
) {

    val tempList = mutableListOf<FhirResourceConfig>().apply {
        secondaryResources.forEach { add(it) }
    }
    var localSecondaryResources by remember { mutableStateOf<List<FhirResourceConfig>>(tempList) }

    localSecondaryResources.forEachIndexed { index, item ->
        Button(
            modifier = Modifier.fillMaxWidth(),
            label = "${index + 1} - Fhir Resource Config",
            onClick = {
                navigator.pushState(
                    ConfigComponentState.FhirResource(
                        title = "${index + 1} - Fhir Resource",
                        fhirResourceConfig = item,
                        navigator = navigator
                    )
                )
            }
        )
        Spacer(Modifier.height(6.dp))
    }

    Spacer(Modifier.height(12.dp))
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.End
    ) {
        CreateButton(
            text = "Add Fhir Resource",
            onClick = {
                secondaryResources.add(FhirResourceConfig())
                localSecondaryResources = mutableListOf<FhirResourceConfig>().apply {
                    addAll(secondaryResources)
                }
            }
        )
    }
}

@Composable
fun IdFieldView(id: String?, modifier: Modifier = Modifier) {
    var idValue by remember { mutableStateOf(id ?: "") }
    OutlinedTextField(
        idValue,
        onValueChange = { idValue = it },
        modifier = modifier
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResourceTypeDropdownView(resourceConfig: ResourceConfig, modifier: Modifier = Modifier) {

    var expanded by remember { mutableStateOf(false) }

    val options = ResourceType.entries.map { it.name }
    //var type by remember { mutableStateOf(resourceConfig.resource.name) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = modifier
    ) {
        OutlinedTextField(
            value = resourceConfig.resource.name,
            onValueChange = { /*type = it*/ },
            label = { Text("Resource Type") },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(
                    expanded = expanded
                )
            },
            modifier = Modifier.menuAnchor().fillMaxWidth()
        )

        //val filteredOptions = options.filter { it.contains(type, ignoreCase = true) }
        //if (filteredOptions.isNotEmpty()) {
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = {
                expanded = false
            }
        ) {
            options.forEach { selectionOption ->
                DropdownMenuItem(
                    text = {
                        Text(text = selectionOption)
                    },
                    onClick = {
                        //type = selectionOption
                        resourceConfig.resource = ResourceType.fromCode(selectionOption)
                        expanded = false
                    }
                )
            }
            //}
        }

    }
}

@Composable
fun CreateButton(
    modifier: Modifier = Modifier,
    text: String,
    onClick: () -> Unit
) {
    Button(
        modifier = modifier,
        label = text,
        icon = Icons.Filled.Add,
        /*colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.secondary,
            contentColor = MaterialTheme.colorScheme.onSecondary
        ),*/
        onClick = onClick
    )
}

@Composable
private fun SmallIconButton(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            //.minimumInteractiveComponentSize()
            .height(22.dp)
            .width(22.dp)
            .clickable(
                onClick = onClick,
                interactionSource = remember { MutableInteractionSource() },
                indication = rememberRipple(bounded = false, radius = 18.dp)
            ),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            modifier = Modifier.width(22.dp),
            imageVector = icon,
            contentDescription = null
        )
    }
}

