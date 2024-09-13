package org.smartregister.fct.device_database.ui.components

import com.arkivanov.decompose.ComponentContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.smartregister.fct.common.data.datatable.controller.DataTableController
import org.smartregister.fct.device_database.domain.model.QueryRequest
import org.smartregister.fct.device_database.domain.model.QueryResponse

class QueryResultDTController(
    private val queryComponent: QueryTabComponent,
    private val persistedQuery: String,
    val queryResponse: QueryResponse
) : DataTableController(
    initialQuery = persistedQuery,
    columns = queryResponse.columns,
    data = queryResponse.data,
    count = queryResponse.count,
    initialLimit = queryComponent.limit
), ComponentContext by queryComponent {

    override fun runQuery(offset: Int, limit: Int) {
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
                    update(offset, response.data)
                } else {
                    showError(response.error)
                }
            }
        }
    }
}