package org.smartregister.fct.presentation.component

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.slot.ChildSlot
import com.arkivanov.decompose.router.slot.SlotNavigation
import com.arkivanov.decompose.router.slot.activate
import com.arkivanov.decompose.router.slot.childSlot
import com.arkivanov.decompose.value.Value
import org.smartregister.fct.common.domain.model.Config
import org.smartregister.fct.common.presentation.component.RootComponent
import org.smartregister.fct.common.presentation.component.ScreenComponent
import org.smartregister.fct.fm.presentation.components.FileManagerScreenComponent
import org.smartregister.fct.sm.presentation.component.StructureMapScreenComponent

class RootComponentImpl(componentContext: ComponentContext) :
    RootComponent, ComponentContext by componentContext {

    private val navigation = SlotNavigation<Config>()

    override val slot: Value<ChildSlot<*, ScreenComponent>> = childSlot(
        source = navigation,
        serializer = Config.serializer(),
        initialConfiguration = {
            Config.DataSpecification
        },
        key = "MainRoot"
    ) { config, childComponentContext ->

        when (config) {
            is Config.DataSpecification -> DataSpecificationScreenComponent(childComponentContext)
            is Config.StructureMap -> StructureMapScreenComponent(childComponentContext)
            is Config.FileManager -> FileManagerScreenComponent(childComponentContext)
        }

    }

    override fun changeSlot(item: Config) {
        navigation.activate(item)
    }

}