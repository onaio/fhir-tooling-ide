package org.smartregister.fct.device_database.ui.components

import com.arkivanov.decompose.ComponentContext
import org.smartregister.fct.device_database.domain.model.TableInfo

internal class TableTabComponent(
    componentContext: ComponentContext,
    val tableInfo: TableInfo,
) : TabComponent(componentContext) {

}