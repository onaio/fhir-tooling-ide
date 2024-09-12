package org.smartregister.fct.device_database.ui.components

import androidx.compose.ui.text.input.TextFieldValue
import com.arkivanov.decompose.ComponentContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.smartregister.fct.adb.domain.model.Device
import org.smartregister.fct.adb.domain.model.PackageInfo
import org.smartregister.fct.device_database.data.persistence.DeviceDBConfigPersistence
import org.smartregister.fct.device_database.domain.model.DBInfo
import org.smartregister.fct.device_database.domain.model.QueryRequest
import org.smartregister.fct.device_database.domain.model.QueryResponse

class QueryTabComponent(
    componentContext: ComponentContext,
) : TabComponent(componentContext), QueryDependency {

    var selectedDBInfo = DeviceDBConfigPersistence.sidePanelDBInfo

    private var _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    private var _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    private var _query = MutableStateFlow(TextFieldValue("select * from ResourceEntity"))
    val query: StateFlow<TextFieldValue> = _query

    private val _queryResultDTController = MutableStateFlow<QueryResultDTController?>(null)
    val queryResultDTController: StateFlow<QueryResultDTController?> = _queryResultDTController

    var limit = 50

    fun updateTextField(textFieldValue: TextFieldValue) {
        CoroutineScope(Dispatchers.Default).launch {
            _query.emit(textFieldValue)
        }
    }

    fun runQuery() {
        if (_query.value.text.trim().isEmpty() || _loading.value) return

        getRequiredParam { device, pkg ->
            CoroutineScope(Dispatchers.IO).launch {
                _loading.emit(true)
                val result = device.runAppDBQuery(
                    packageId = pkg.packageId,
                    requestJson = QueryRequest(
                        database = selectedDBInfo.name,
                        query = _query.value.text,
                    ).asJSONString()
                )

                _loading.emit(false)
                _queryResultDTController.emit(
                    QueryResultDTController(
                        queryComponent = this@QueryTabComponent,
                        persistedQuery = _query.value.text,
                        queryResponse = QueryResponse.build(result)
                    )
                )
            }
        }
    }

    override fun getRequiredParam(
        showErrors: Boolean,
        info: (Device, PackageInfo) -> Unit
    ) {
        if (componentContext is QueryDependency) {
            componentContext.getRequiredParam(showErrors, info)
        }
    }
}