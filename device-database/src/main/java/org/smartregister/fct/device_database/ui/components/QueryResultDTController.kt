package org.smartregister.fct.device_database.ui.components

import com.arkivanov.decompose.ComponentContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.json.JSONObject
import org.smartregister.fct.common.data.datatable.controller.DataTableController
import org.smartregister.fct.common.data.datatable.feature.DTPagination
import org.smartregister.fct.device_database.domain.model.QueryRequest
import org.smartregister.fct.device_database.domain.model.QueryResponse

class QueryResultDTController(
    private val queryComponent: QueryTabComponent,
    private val persistedQuery: String,
    val queryResponse: QueryResponse
) : DataTableController, DTPagination, ComponentContext by queryComponent {

    private var _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    private var _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    private var _query = MutableStateFlow(persistedQuery)
    val query: StateFlow<String> = _query

    private var _data = MutableStateFlow(queryResponse.data)
    override val data: StateFlow<List<JSONObject>> = _data

    override val columns: List<JSONObject> = queryResponse.columns

    private var offset: Int = 0

    override fun refreshData() {

    }

    override fun getOffset(): Int = offset

    override fun getLimit() = queryComponent.limit

    override fun updateLimit(limit: Int) {
        queryComponent.limit = limit
    }

    override fun goNext(offset: Int) {
        runQuery(
            offset = offset,
            limit = getLimit()
        )
    }

    override fun totalRecords(): Int = queryResponse.count

    private fun runQuery(offset: Int, limit: Int) {
        queryComponent.getRequiredParam { device, pkg ->
            CoroutineScope(Dispatchers.IO).launch {
                val result = device.runAppDBQuery(
                    packageId = pkg.packageId,
                    requestJson = QueryRequest(
                        database = queryComponent.selectedDBInfo.name,
                        query = persistedQuery,
                        offset = offset,
                        limit = limit
                    ).asJSONString()
                )

                val response = QueryResponse.build(result)
                if (response.error == null) {
                    this@QueryResultDTController.offset = offset
                    _data.emit(response.data)
                } else {
                    showError(response.error)
                }
            }
        }
    }

    private suspend fun showError(message: String) {
        _error.emit(message)
        delay(200)
        _error.emit(null)
    }
}