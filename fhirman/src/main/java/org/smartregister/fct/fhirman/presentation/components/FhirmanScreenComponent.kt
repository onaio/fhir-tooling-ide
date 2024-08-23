package org.smartregister.fct.fhirman.presentation.components

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.smartregister.fct.common.data.manager.AppSettingManager
import org.smartregister.fct.common.domain.model.ServerConfig
import org.smartregister.fct.common.presentation.component.ScreenComponent
import org.smartregister.fct.common.util.componentScope

class FhirmanScreenComponent(
    componentContext: ComponentContext
) : ScreenComponent, ComponentContext by componentContext {

}