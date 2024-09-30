package org.smartregister.fct.sm.presentation.component

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.smartregister.fct.common.presentation.component.ScreenComponent
import org.smartregister.fct.common.util.windowTitle
import org.smartregister.fct.engine.util.componentScope
import org.smartregister.fct.engine.util.getKoinInstance
import org.smartregister.fct.sm.domain.model.SMDetail
import org.smartregister.fct.sm.domain.model.TabIndex
import org.smartregister.fct.sm.domain.usecase.CreateNewSM
import org.smartregister.fct.sm.domain.usecase.DeleteSM
import org.smartregister.fct.sm.domain.usecase.GetAllSM


class StructureMapScreenComponent(private val componentContext: ComponentContext) : ScreenComponent,
    KoinComponent, ComponentContext by componentContext {

    private val getAllStructureMaps: GetAllSM by inject()
    private val createNewSM: CreateNewSM by inject()
    private val deleteStructureMap: DeleteSM by inject()
    private val tabIndex: TabIndex = getKoinInstance()

    private var _activeTabIndex = MutableValue(tabIndex.index)
    val activeTabIndex: Value<Int> = _activeTabIndex

    private val _smTabComponents = MutableValue<List<StructureMapTabComponent>>(listOf())
    val smTabComponents: Value<List<StructureMapTabComponent>> = _smTabComponents


    private val _showStructureMapDeleteDialog = MutableValue(Pair(false, -1))
    val showStructureMapDeleteDialog: Value<Pair<Boolean, Int>> = _showStructureMapDeleteDialog

    init {
        componentScope.launch {
            windowTitle.emit("StructureMap Transformation")
        }
        loadAllStructureMaps()
    }

    private fun loadAllStructureMaps() {
        componentScope.launch {
            getAllStructureMaps().collectLatest {
                if (it.isNotEmpty() && tabIndex.index >= it.size) updateTabIndex(it.size - 1)
                _smTabComponents.value = it.map {
                    StructureMapTabComponent(componentContext, it)
                }
            }
        }
    }

    fun insertNewStructureMap(smDetail: SMDetail) {
        componentScope.launch {
            createNewSM(smDetail)
        }
    }

    fun changeTab(index: Int) {
        updateTabIndex(index)
    }

    fun showDeleteStructureMapDialog(index: Int) {
        componentScope.launch {
            _showStructureMapDeleteDialog.value = Pair(true, index)
        }
    }

    fun hideDeleteStructureMapDialog() {
        _showStructureMapDeleteDialog.value = Pair(false, -1)
    }


    fun closeTab() {
        componentScope.launch {
            val deletedTabIndex = showStructureMapDeleteDialog.value.second
            hideDeleteStructureMapDialog()
            deleteStructureMap(smTabComponents.value[deletedTabIndex].smDetail.id)
        }
    }

    private fun updateTabIndex(index: Int) {
        tabIndex.index = index
        _activeTabIndex.value = index
    }
}