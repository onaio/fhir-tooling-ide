package org.smartregister.fct.common.data.datatable.controller

import kotlinx.coroutines.flow.StateFlow
import org.json.JSONObject

interface DataTableController {

    val columns: List<JSONObject>
    val data: StateFlow<List<JSONObject>>

    fun refreshData()
}