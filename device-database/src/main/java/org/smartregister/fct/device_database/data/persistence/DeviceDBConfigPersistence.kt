package org.smartregister.fct.device_database.data.persistence

import org.smartregister.fct.common.data.controller.TabsControllerImpl
import org.smartregister.fct.common.domain.model.TabType
import org.smartregister.fct.device_database.domain.model.DBInfo
import org.smartregister.fct.device_database.domain.model.TableInfo
import org.smartregister.fct.device_database.ui.components.QueryTabComponent
import org.smartregister.fct.device_database.ui.components.TabComponent
import org.smartregister.fct.device_database.ui.components.TableTabComponent

internal object DeviceDBConfigPersistence {

    val controller = TabsControllerImpl(
        items = listOf<TabComponent>(),
        title = { index, tab ->
            when (tab) {
                is QueryTabComponent -> "Query [${(index + 1)}]"
                is TableTabComponent -> tab.tableInfo.name
                else -> "$index"
            }
        },
        tabType = TabType.Scrollable
    )

    val listOfDB = listOf(
        DBInfo(
            name = "resources.db",
            label = "Resource"
        ),
        DBInfo(
            name = "knowledge.db",
            label = "Knowledge"
        )
    )

    var sidePanelDBInfo: DBInfo = listOfDB[0]

    val tablesMap = mutableMapOf<String, List<TableInfo>>(
        Pair(listOfDB[0].name, listOf()),
        Pair(listOfDB[1].name, listOf()),
    )

    fun addNewTab(tabComponent: TabComponent) {
        controller.add(tabComponent)
    }
}